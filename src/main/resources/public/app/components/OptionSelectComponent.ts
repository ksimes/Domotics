/**
 * Created by S.King on 22/07/2016.
 */

import {Component, Input, OnChanges, SimpleChange} from "@angular/core";

import {DataStationService} from "../services/station.services";

import {DisplayChartComponent} from "./DisplayChartComponent";

import {Configuration} from "../configuration";
import {Station} from "../models/Station.ts";
import {Option} from "../models/Option.ts";
import {DisplayOptions} from "../models/DisplayOptions";

@Component({
  selector: 'options-component',
  providers: [Configuration, DataStationService],
  templateUrl: 'app/templates/DisplayOptions.Component.html',
  directives: [DisplayChartComponent]
})
export class DisplayOptionsComponent {
  title = 'display options';
  options:Option[];
  stationData:Station[];
  config:DisplayOptions;

  constructor(private _dataStationService:DataStationService, private _configuration:Configuration) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
    console.log('Set this.config');
    this.getStationInformation();
  }

  private onSelectTemp():void {
    if (this.config.showTemp) {
      this.config = new DisplayOptions(this.config.displayId, 1, false, this.config.showHumidity, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, 1, true, this.config.showHumidity, this.config.showHeatIndex);
    }
  }

  private onSelectHumidity():void {
    if (this.config.showHumidity) {
      this.config = new DisplayOptions(this.config.displayId, 1, this.config.showTemp, false, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, 1, this.config.showTemp, true, this.config.showHeatIndex);
    }
  }

  private onSelectHeatIndex():void {
    if (this.config.showHeatIndex) {
      this.config = new DisplayOptions(this.config.displayId, 1, this.config.showTemp, this.config.showHumidity, false);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, 1, this.config.showTemp, this.config.showHumidity, true);
    }
  }

  private onSelectDisplay():void {
    this.config = new DisplayOptions(this.config.displayId, 1, this.config.showTemp, this.config.showHumidity, this.config.showHeatIndex);
  }

  private getStationInformation():void {
    this._dataStationService
        .GetAllStations()
        .subscribe((data:Station[]) => this.stationData = data,
            error => console.log(error),
            () => {
              console.log('Get all station data complete');
            }
        );
  }
}
