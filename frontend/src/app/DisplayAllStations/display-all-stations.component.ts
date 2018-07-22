import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {ViewChild} from "@angular/core";
import {DataStationService} from '../services/station.services';
import {Configuration} from '../models/configuration';
import {DisplayOptions} from '../models/DisplayOptions';
import {Option} from '../models/Option';
import {Station} from '../models/Station';
import {DataSensorTypeService} from '../services/sensortype.services';
import {SensorType} from '../models/SensorType';
import * as moment from 'moment';
import {DisplayStationService} from "../services/display-station.service";
import {StationDisplay} from "../models/station-display";
import {GridOptions} from "ag-grid";
import {AgGridNg2} from "ag-grid-angular";
import {WarningComponent} from "./components/warning/warning.component";

@Component({
  selector: 'display-all-stations',
  templateUrl: './display-all-stations.component.html',
  styleUrls: ['./display-all-stations.component.css'],
  providers: [Configuration, DataStationService, DataSensorTypeService, DisplayStationService],
})

export class DisplayAllStations implements OnInit {
  @ViewChild('agGrid0') agGrid: AgGridNg2;
  title = 'Domotics';
  now = DisplayAllStations.getFormattedDate();
  errorMsg: string = '';
  timeStamp: string = '';
  config: DisplayOptions;
  stationData: StationDisplay[] = [];

  sensorData: SensorType[];
  options: Option[];

  private gridApi;
  public gridOptions: GridOptions;

  columnDefs = [
    {headerName: 'Name', field: 'name'},
    {headerName: 'Description', field: 'description'},
    {headerName: 'Temperature &deg;C', field: 'temperature'},
    {headerName: 'Humidity %', field: 'humidity'},
    {headerName: 'Heat Index', field: 'heatIndex'},
    {headerName: 'Last result gathered', field: 'timeStamp', cellRendererFramework: WarningComponent,}
  ];

  rowData = [];
  private params: any;

  constructor(private _dataStationService: DataStationService, private _dataSensorService: DataSensorTypeService,
              public _configuration: Configuration, private displayStationService: DisplayStationService) {
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
    this.refresh();
  }

  agInit(params: any): void {
    this.params = params;
  }

  onGridReady() {
    this.agGrid.api.refreshCells();
    this.agGrid.api.sizeColumnsToFit();
  }

  public refresh(): void {
    this.errorMsg = '';
    this.getSensorInformation();
    this.getStationInformation('1');
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

  private getStationInformation(selectedSensorType: string): void {
    let stations: Station[] = [];
    this._dataStationService.getAllStations()
      .subscribe(
        (data: Station[]) => {
          stations = data;
        },
        error => this.handleError(error),
        () => {
          stations.map((entry) => {

            this.displayStationService.getStationData(entry).subscribe(stationData => {
                this.stationData.push(stationData);
              },
              error => this.handleError(error),
              () => {
                this.rowData = this.filteredByType(selectedSensorType);
              }
            );
          });

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

  public filteredByType(selectedSensorType: string): StationDisplay[] {
    if (this.stationData && this.stationData.length > 1) {
      return this.stationData.filter((station) =>
        station.station.sensorType === selectedSensorType
      ).sort(function (obj1, obj2) {
        // Ascending: first name less than the previous
        return +(obj1.name > obj2.name);
      });
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
