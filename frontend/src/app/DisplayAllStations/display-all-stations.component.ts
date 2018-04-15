import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {DataStationService} from '../services/station.services';
import {Configuration} from '../models/configuration';
import {DisplayOptions} from '../models/DisplayOptions';
import {Option} from '../models/Option';
import {Station} from '../models/Station';
import {DataSensorTypeService} from '../services/sensortype.services';
import {SensorType} from '../models/SensorType';
import * as moment from 'moment';

@Component({
  selector: 'display-all-stations',
  templateUrl: './display-all-stations.component.html',
  styleUrls: ['./display-all-stations.component.css'],
  providers: [Configuration, DataStationService, DataSensorTypeService],
})

export class DisplayAllStations implements OnInit {
  title = 'Domotics';
  now = DisplayAllStations.getFormattedDate();
  errorMsg: string = '';
  timeStamp: string = '';
  config: DisplayOptions;
  stationData: Station[];
  sensorData: SensorType[];
  options: Option[];

  constructor(private _dataStationService: DataStationService, private _dataSensorService: DataSensorTypeService, public _configuration: Configuration) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
  }

  ngOnInit(): void {
    this.refresh();
  }

  public refresh(): void {
    this.errorMsg = '';
    this.getSensorInformation();
    this.getStationInformation();
    this.now = DisplayAllStations.getFormattedDate();
  }

  private getSensorInformation(): void {
    this._dataSensorService.GetAllSensorTypes()
      .subscribe((data: SensorType[]) => this.sensorData = data,
        error => console.log(error),
        () => {
          console.log('Get all sensor data complete');
        }
      );
  }

  private getStationInformation(): void {
    this._dataStationService.getAllStations()
      .subscribe((data: Station[]) => this.stationData = data,
        error => console.log(error),
        () => {
          console.log('Get all station data complete');
        }
      );
  }

  public getSensorName(selectedSensorType: string): String {

    if (this.sensorData) {

      return this.sensorData[selectedSensorType].name;
    }

    return null;
  }


  public filteredByType(selectedSensorType: string): Station[] {
    if (this.stationData && this.stationData.length > 1) {
      return this.stationData.filter((station) =>
        station.sensorType === selectedSensorType
      );
    }

    return this.stationData;
  }

  public static getFormattedDate(): string {
    let date: Date = new Date();
    // console.log('date   ' + date);
    return moment(date).format('dddd, MMMM Do YYYY, h:mm:ss a')
  }

  private handleError(error: any): string {
    let errMsg = "No data available for this period";
    if (error.status != 416) {
      let errMsg = (error.message) ? error.message :
        error.status ? `${error.status} - ${error.statusText}` : 'Server error';
      console.log(' ' + error.status + ' : ' + errMsg);     // log to console
    } else {
      console.log(error);
    }

    return errMsg;
  }
}
