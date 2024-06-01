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
2.6 - Yves Mercier - initial version

*/

metadata
    {
    definition(name: "HADB Generic Component Humidifier", namespace: "community", author: "community", importUrl: "https://raw.githubusercontent.com/ymerj/HE-HA-control/main/HADBgenericComponentHumidifier.groovy")
        {
        capability "Switch"
        capability "Refresh"
        capability "Actuator"
        capability "Health Check"
        }
    preferences
        {
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
        }

    command "setMode", [[name: "modeNumber", type: "number", description: "Indicate the index of the desired mode"]]

    attribute "healthStatus", "enum", ["offline", "online"]
    attribute "supportedModes", "string"
    attribute "humidifierMode", "string"
    }

void updated() {
    log.info "Updated..."
    log.warn "description logging is: ${txtEnable == true}"
}

void installed() {
    log.info "Installed..."
    device.updateSetting("txtEnable",[type:"bool",value:true])
    refresh()
}

void parse(String description) { log.warn "parse(String description) not implemented" }

void parse(List description) {
    description.each {
        if (it.name in ["switch", "mode", "supportedModes", "healthStatus"]) {
            if (txtEnable) log.info it.descriptionText
            sendEvent(it)
        }
    }
}

void on() {
    parent?.componentOn(this.device)
}

void off() {
    parent?.componentOff(this.device)
}

def setMode(modeNumber) {
    parent?.componentSetMode(this.device, modeNumber)
}

void refresh() {
    parent?.componentRefresh(this.device)
}

def ping() {
    refresh()
}
