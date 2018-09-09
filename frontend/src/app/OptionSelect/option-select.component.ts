/**
 * Created by S.King on 22/07/2016.
 */

import {Component, OnInit} from '@angular/core';
import {OnChanges} from '@angular/core';
import {SimpleChanges} from '@angular/core';
import {DataStationService} from '../services/station.services';
import {Configuration} from '../models/configuration';
import {Station} from '../models/Station';
import {Option} from '../models/Option';
import {DisplayOptions} from '../models/DisplayOptions';
import {ActivatedRoute, ParamMap} from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {DisplayStationService} from "../services/display-station.service";

@Component({
  selector: 'options-component',
  providers: [Configuration, DataStationService],
  templateUrl: './option-select.component.html',
})

export class OptionSelectComponent implements OnChanges, OnInit {
  now = DisplayStationService.getFormattedDate();
  title = 'display options';
  errorMsg: string = '';
  options: Option[];
  stationData: Station;
  config: DisplayOptions;

  constructor(private dataStationService: DataStationService, public _configuration: Configuration, private route: ActivatedRoute) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  ngOnInit(): void {
    let stationId: string;

    this.route.paramMap
      .switchMap((params: ParamMap) => {
        stationId = params.get('id');
        return this.dataStationService.getStation(stationId);
      })
      .subscribe((data: Station) => this.stationData = data);

    this.config.stationId = stationId;
    this.now = DisplayStationService.getFormattedDate();
  }

  public getName(): String {
    if (this.stationData) {
      return this.stationData.name;
    }

    return null;
  }

  public refresh(): void {
    this.now = DisplayStationService.getFormattedDate();
  }

  public onSelectTemp(): void {
    if (this.config.showTemp) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, false, this.config.showHumidity, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, true, this.config.showHumidity, this.config.showHeatIndex);
    }
  }

  public onSelectHumidity(): void {
    if (this.config.showHumidity) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, false, this.config.showHeatIndex);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, true, this.config.showHeatIndex);
    }
  }

  public onSelectHeatIndex(): void {
    if (this.config.showHeatIndex) {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, this.config.showHumidity, false);
    }
    else {
      this.config = new DisplayOptions(this.config.displayId, this.config.stationId, this.config.showTemp, this.config.showHumidity, true);
    }
  }

  public onSelectDisplay(newValue): void {
    console.log('onSelectDisplay ' + newValue);
    this.config = new DisplayOptions(newValue, this.config.stationId, this.config.showTemp, this.config.showHumidity, this.config.showHeatIndex);
  }

  public onSelectStation(newValue): void {
    console.log('onSelectStation ' + newValue);
    this.config = new DisplayOptions(this.config.displayId, newValue, this.config.showTemp, this.config.showHumidity, this.config.showHeatIndex);
  }
}
