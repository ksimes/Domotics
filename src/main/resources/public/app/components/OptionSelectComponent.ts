/**
 * Created by S.King on 22/07/2016.
 */

import { Component } from '@angular/core';

import {DataStationService} from "../services/station.services";

import {DisplayChartComponent} from "./DisplayChartComponent";

import {Configuration} from "../configuration";
import {DisplayOptions} from "../models/DisplayOptions.ts";
import {Station} from "../models/Station.ts";
import {Option} from "../models/Option.ts";

@Component({
  selector: 'options-component',
  providers: [Configuration, DataStationService],
  templateUrl: 'app/templates/DisplayOptions.Component.html',
  directives: [DisplayChartComponent]
})
export class DisplayOptionsComponent {
  title = 'display options';
  options : Option[];
  currentState: DisplayOptions;
  stationData:Station[];

  constructor(private _dataStationService:DataStationService, private _configuration:Configuration) {
    this.options = _configuration.display;
    this.currentState = _configuration.initialState;
    this.getStationInformation();
  }

  private getStationInformation():void {
    this._dataStationService
        .GetAllStations()
        .subscribe((data:Station[]) => this.stationData = data,
            error => console.log(error),
            () => {
              console.log('Get station data complete');
            }
        );
  }
}
