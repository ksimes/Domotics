import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {ViewChild} from "@angular/core";
import {DataStationService} from '../services/station.services';
import {Configuration} from '../models/configuration';
import {DisplayOptions} from '../models/DisplayOptions';
import {Option} from '../models/Option';
import {DataSensorTypeService} from '../services/sensortype.services';
import {SensorType} from '../models/SensorType';
import * as moment from 'moment';
import {Moment} from "moment";
import {DisplayStationService} from "../services/display-station.service";
import {CacheDisplay} from "../models/cache-display";
import {GridOptions} from "ag-grid";
import {AgGridNg2} from "ag-grid-angular";
import {WarningComponent} from "./components/warning/warning.component";
import {SensorCacheService} from "../services/sensorCache.services";
import {SensorCache} from "../models/sensorCache";
import {Station} from "../models/Station";
import {LinkToComponent} from "./components/link-to/link-to.component";

@Component({
  selector: 'display-all-stations',
  templateUrl: './display-all-stations.component.html',
  styleUrls: ['./display-all-stations.component.css'],
  providers: [Configuration, DataStationService, DataSensorTypeService, DisplayStationService],
})

export class DisplayAllStations implements OnInit {
  @ViewChild('agGrid0') agGrid0: AgGridNg2;
  @ViewChild('agGrid1') agGrid1: AgGridNg2;
  title = 'Domotics';
  now = DisplayAllStations.getFormattedDate();
  errorMsg: string = '';
  timeStamp: string = '';
  config: DisplayOptions;
  stationCacheData: CacheDisplay[] = [];

  sensorData: SensorType[];
  stationData: Station[];
  options: Option[];

  private gridApi;
  public gridOptions: GridOptions;

  columnDefs0 = [
    {headerName: 'Name', field: 'station', cellRendererFramework: LinkToComponent},
    {headerName: 'Description', field: 'description'},
    {headerName: 'Temperature &deg;C', field: 'temperature'},
    {headerName: 'Humidity %', field: 'humidity'},
    {headerName: 'Heat Index', field: 'heatIndex'},
    {headerName: 'Last result gathered', field: 'timeStamp', cellRendererFramework: WarningComponent}
  ];

  columnDefs1 = [
    {headerName: 'Name', field: 'station', cellRendererFramework: LinkToComponent},
    {headerName: 'Description', field: 'description'},
    {headerName: 'Temperature &deg;C', field: 'temperature'},
    {headerName: 'Humidity %', field: 'humidity'},
    {headerName: 'Heat Index', field: 'heatIndex'},
    {headerName: 'Last result gathered', field: 'timeStamp', cellRendererFramework: WarningComponent}
  ];

  rowData0 = [];    // Used for the temp/humidity sensors
  rowData1 = [];    // Used for the plant sensors
  private params: any;

  constructor(private _sensorCacheService: SensorCacheService, private _dataSensorService: DataSensorTypeService,
              public _configuration: Configuration, private _stationService: DataStationService) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;

    this.gridOptions = <GridOptions>{
      onGridReady: () => {
        this.gridApi = this.gridOptions.api;
        this.gridOptions.api.sizeColumnsToFit();
      }
    };
  }

  ngOnInit(): void {
    this.getSensorInformation();
    this.getStationInformation();
    this.refresh();
  }

  agInit(params: any): void {
    this.params = params;
  }

  onGridReady() {
  }

  public refresh(): void {
    this.errorMsg = '';
    this.getStationCacheInformation('1').then(data => {
        this.rowData0 = data;
        this.agGrid0.api.refreshCells();
        this.agGrid0.api.sizeColumnsToFit();
      }
    );
    this.getStationCacheInformation('2').then(data => {
      this.rowData1 = data;
      this.agGrid1.api.refreshCells();
      this.agGrid1.api.sizeColumnsToFit();
    });
    this.now = DisplayAllStations.getFormattedDate();
  }

  private getSensorInformation(): void {
    this._dataSensorService.getAllSensorTypes()
      .subscribe((data: SensorType[]) => this.sensorData = data,
        error => console.log(error),
        () => {
          console.log('Get all sensor data complete');
        }
      );
  }

  private getStationInformation(): void {
    this._stationService.getAllStations()
      .subscribe((data: Station[]) => this.stationData = data,
        error => console.log(error),
        () => {
          console.log('Get all station data complete');
        }
      );
  }

  private static processTimeStamp(timeStamp: string): string {

    const year: number = +timeStamp.substr(0, 4);
    const month: number = +timeStamp.substr(5, 2);
    const day: number = +timeStamp.substr(8, 2);
    const hour: number = +timeStamp.substr(11, 2);
    const minute: number = +timeStamp.substr(14, 2);
    const second: number = +timeStamp.substr(17, 2);

    const date: Date = new Date(year, month - 1, day, hour, minute, second);

    const timestamp: Moment = moment(date);
    let result: string;

    // console.log('Difference:  ' + timestamp.diff(moment(), 'minutes'));

    if (Math.abs(timestamp.diff(moment(), 'minutes')) > 30) {
      result = 'More than 30 minutes ago';
    }
    else {
      result = moment(date).format('ddd, MMM Do YYYY, HH:mm:ss');
    }

    return result;
  }


  private getStationCacheInformation(selectedSensorType: string): Promise<CacheDisplay[]> {
    return new Promise<CacheDisplay[]>((resolve, reject) => {
      let stations: SensorCache[] = [];
      let finalData: CacheDisplay[] = [];

      this._sensorCacheService.getSensorCache()
        .subscribe((data: SensorCache[]) => stations = data,
          error => console.log(error),
          () => {
            stations.filter((item: SensorCache) => item.sensorType === selectedSensorType).map((datum: SensorCache) => {
              let result: CacheDisplay = new CacheDisplay(datum.stationId, datum.name, datum.description, datum.sensorType);
              result.station = this.stationData.find(stuff => stuff._key === datum.stationId);
              result.temperature = '-';
              result.humidity = '-';
              result.heatIndex = '-';

              result.timeStamp = DisplayAllStations.processTimeStamp(datum.timeStamp);
              if (!result.timeStamp.startsWith('More')) {
                result.temperature = `${datum.temperature} \u00B0C`;
                result.humidity = `${datum.humidity}%`;
                result.heatIndex = `${datum.heatIndex}`;
              }

              finalData.push(result);
            });

            console.log('Get all sensor cache data complete');
            resolve(finalData);
          }
        )
    });
  }

  public getSensorName(selectedSensorType: string): String {
    if (this.sensorData) {
      return this.sensorData[selectedSensorType].name;
    }
    return null;
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
