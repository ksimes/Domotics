/**
 * Created by S.King on 22/07/2016.
 */

import {Component, Input, OnChanges, SimpleChange} from "@angular/core";

import {DataStationService} from "../services/station.services";

import {DisplayChartComponent} from "./DisplayChartComponent";
import {DisplayStationComponent} from "./DisplayStationComponent";

import {Configuration} from "../configuration";
import {Station} from "../models/Station";
import {Option} from "../models/Option";
import {DisplayOptions} from "../models/DisplayOptions";

@Component({
  selector: 'options-component',
  providers: [Configuration, DataStationService],
  templateUrl: 'app/templates/DisplayOptions.Component.html',
  directives: [DisplayChartComponent, DisplayStationComponent]
})

export class DisplayOptionsComponent {
  title = 'display options';
  options:Option[];
  stationData:Station[];
  config:DisplayOptions;

  constructor(private _dataStationService:DataStationService, private _configuration:Configuration) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
    this.refresh();
  }

  private refresh():void {
    this.getStationInformation();
  }
  
  private onSelectTemp():void {
    if (this.config.showTemp) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, false, this.config.showHumidity, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, true, this.config.showHumidity, this.config.showHeatIndex);
    }
  }

  private onSelectHumidity():void {
    if (this.config.showHumidity) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, false, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, true, this.config.showHeatIndex);
    }
  }

  private onSelectHeatIndex():void {
    if (this.config.showHeatIndex) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, this.config.showHumidity, false);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, this.config.showHumidity, true);
    }
  }

  private onSelectDisplay(newValue):void {
    console.log('onSelectDisplay ' + newValue);
    this.config = new DisplayOptions(newValue, this.config.stationId, this.config.showTemp, this.config.showHumidity, this.config.showHeatIndex);
  }

  private onSelectStation(newValue):void {
    console.log('onSelectStation ' + newValue);
    this.config = new DisplayOptions(this.config.displayId, newValue, this.config.showTemp, this.config.showHumidity, this.config.showHeatIndex);
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
