/*

Copyright 2024

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

-------------------------------------------

Change history:

0.1  - ritchierich - initial version
2.4  - Yves Mercier - Modified healthCheck handling
2.7  - Yves Mercier - Add support for presets
2.8  - mluck - corrected typo
2.12 - Yves Mercier -  Add presets by name

*/

metadata
{
    definition(name: 'HADB Generic Component Thermostat', namespace: 'community', author: 'community')
    {
        capability 'Actuator'
        capability 'Sensor'
        capability 'TemperatureMeasurement'
        capability 'Thermostat'
        capability 'RelativeHumidityMeasurement'
        capability 'Refresh'
        capability "Health Check"
    }
    preferences
    {
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
    }

    command "setPreset", [[name: "preset", type: "NUMBER", description: "Preset number"]]
    command "setPresetName", [[name: "presetName", type: "STRING", description: "Preset name"]]
    command "setPresetNumber", [[name: "presetNumber", type: "NUMBER", description: "Preset number"]]
    
    attribute "healthStatus", "enum", ["offline", "online"]
    attribute "supportedThermostatFanModes", "JSON_OBJECT"
    attribute "supportedThermostatModes", "JSON_OBJECT"
    attribute "supportedPresets", "string"
    attribute "currentPreset", "string"
}

void installed() {
    log.info "Installed..."
    device.updateSetting("txtEnable",[type:"bool",value:true])
    refresh()
}

void updated() {
    log.info "Updated..."
    if (logEnable) runIn(1800,logsOff)
}

void uninstalled() {
    log.info "${device} driver uninstalled"
}

void parse(String description) { log.warn "parse(String description) not implemented" }

void parse(List<Map> description) {
    description.each {
        if (txtEnable) log.info it.descriptionText
        sendEvent(it)
    }
}

void off() {
    parent?.componentOff(this.device)
}

void refresh() {
    parent?.componentRefresh(this.device)
}

void setCoolingSetpoint(BigDecimal temperature) {
    parent?.componentSetCoolingSetpoint(this.device, temperature)
}

void setHeatingSetpoint(BigDecimal temperature) {
    parent?.componentSetHeatingSetpoint(this.device, temperature)
}

void setThermostatMode(String thermostatMode) {
    parent?.componentSetThermostatMode(this.device, thermostatMode)
}

void setThermostatFanMode(String fanMode) {
    parent?.componentSetThermostatFanMode(this.device, fanMode)
}

void auto() {
    parent?.componentAuto(this.device)
}

void cool() {
    parent?.componentCool(this.device)
}

void emergencyHeat() {
    parent?.componentEmergencyHeat(this.device)
}

void heat() {
    parent?.componentHeat(this.device)
}

void fanAuto() {
    parent?.componentFanAuto(this.device)
}

void fanCirculate() {
    parent?.componentFanCirculate(this.device)
}

void fanOn() {
    parent?.componentFanOn(this.device)
}

def setPreset(preset){
    setPresetNumber(preset)
}

def setPresetNumber(presetNumber) {
    if (this.device.currentValue("supportedPresets") == "none") log.warn "no supported presets defined"
    else parent?.componentSetPresetNumber(this.device, presetNumber)
    }

def setPresetName(presetName) {
    if (this.device.currentValue("supportedPresets") == "none") log.warn "no supported presets defined"
    else parent?.componentSetPresetName(this.device, presetName)
    }

def logsOff(){
    log.warn("debug logging disabled...")
    device.updateSetting("logEnable",[value:"false",type:"bool"])
}

def ping() {
    refresh()
}
