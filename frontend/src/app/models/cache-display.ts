import {Station} from "./Station";

export class CacheDisplay {
  private _station: Station;
  private _timeStamp: string;
  private _temperature: string;
  private _humidity: string;
  private _heatIndex: string;
  private _sampleRate: string;

  constructor(private _stationId: string,
              private _name: string,
              private _description: string,
              private _sensorType: string) {
  }

  get stationId(): string {
    return this._stationId;
  }

  set stationId(value: string) {
    this._stationId = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get description(): string {
    return this._description;
  }

  set description(value: string) {
    this._description = value;
  }

  get timeStamp(): string {
    return this._timeStamp;
  }

  set timeStamp(value: string) {
    this._timeStamp = value;
  }

  get temperature(): string {
    return this._temperature;
  }

  set temperature(value: string) {
    this._temperature = value;
  }

  get humidity(): string {
    return this._humidity;
  }

  set humidity(value: string) {
    this._humidity = value;
  }

  get heatIndex(): string {
    return this._heatIndex;
  }

  set heatIndex(value: string) {
    this._heatIndex = value;
  }

  get sampleRate(): string {
    return this._sampleRate;
  }

  set sampleRate(value: string) {
    this._sampleRate = value;
  }

  get sensorType(): string {
    return this._sensorType;
  }

  set sensorType(value: string) {
    this._sensorType = value;
  }

  get station(): Station {
    return this._station;
  }

  set station(value: Station) {
    this._station = value;
  }
}
