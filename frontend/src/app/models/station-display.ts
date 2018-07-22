import {Station} from "./Station";

export class StationDisplay {
  readOk: boolean = false;
  station: Station;
  private _name: string;
  private _description: string;
  private _timeStamp: string;
  private _temperature: string;
  private _humidity: string;
  private _heatIndex: string;
  errorMsg: string = null;

  constructor(station: Station) {
    this.station = station;
    this.name = station.name;
    this.description = station.description;
  }

  set name(name: string) {
    this._name = name;
  }

  get name() {
    return this._name
  }

  set description(description: string) {
    this._description = description;
  }

  get description() {
    return this._description
  }

  set timeStamp(timestamp: string) {
    this._timeStamp = timestamp;
  }

  get timeStamp() {
    return this._timeStamp;
  }

  set temperature(temperature: string) {
    this._temperature = temperature;
  }

  get temperature() {
    return this._temperature;
  }

  set humidity(humidity: string) {
    this._humidity = humidity;
  }

  get humidity() {
    return this._humidity;
  }

  set heatIndex(heatIndex: string) {
    this._heatIndex = heatIndex;
  }

  get heatIndex() {
    return this._heatIndex;
  }
}
