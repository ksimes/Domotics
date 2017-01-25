import {Component, Input, OnChanges, SimpleChange} from "@angular/core";
import {CORE_DIRECTIVES, NgClass, FORM_DIRECTIVES} from "@angular/common";
import {CHART_DIRECTIVES} from "ng2-charts";
import {DataStationService} from "../services/station.services";
import {DataTemperatureService} from "../services/temperature.services";
import {DataHumidityService} from "../services/humidity.services";
import {Measurement} from "../models/Measurement";
import {Station} from "../models/Station";
import {Configuration} from "../configuration";
import {DisplayOptions} from "../models/DisplayOptions";

@Component({
  selector: 'display-chart-component',
  providers: [DataStationService, DataTemperatureService, DataHumidityService, Configuration],
  templateUrl: 'app/templates/DisplayChart.Component.html',
  directives: [CHART_DIRECTIVES, NgClass, CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class DisplayChartComponent implements OnChanges {
  @Input("config")
  config:DisplayOptions;

  station:number = 1;
  stationData:Station;
  errorMsg:string = '';

  constructor(private _dataStationService:DataStationService,
              private _dataTemperatureService:DataTemperatureService, private _dataHumidityService:DataHumidityService) {
    console.log('Configured');
  }

  ngOnChanges(changes:{[propKey:string]:SimpleChange}) {
    // let log:string[] = [];
    // for (let propName in changes) {
    //   let changedProp = changes[propName];
    //   let from = JSON.stringify(changedProp.previousValue);
    //   let to = JSON.stringify(changedProp.currentValue);
    //   log.push(`${propName} changed from ${from} to ${to}`);
    // }
    // console.log(log.join(', '));
    this.refresh(this.config);
  }

  public refresh(config:DisplayOptions) {
    this.errorMsg = '';

    this.station = config.stationId;

    this.getStationInformation(this.station);

    if (config.showTemp) {
      this.getTemperaturesToday(this.station, false, config.displayId);
    }

    if (config.showHumidity) {
      this.getHumidityToday(this.station, true, config.displayId);
    }

    if (config.showHeatIndex) {
      // this.getHeatIndexToday(this.station, true);
    }
  }

  // lineChart
  public lineChartData:Array<any> = [{data: [0], label: 'temp'}];
  public lineChartLabels:Array<any> = ['any'];

  public lineChartOptions:any = {
      animation: false,
      responsive: true,
      maintainAspectRatio: false
    };
  public lineChartColours:Array<any> = [
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    },
    { // dark grey
      backgroundColor: 'rgba(77,83,96,0.2)',
      borderColor: 'rgba(77,83,96,1)',
      pointBackgroundColor: 'rgba(77,83,96,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(77,83,96,1)'
    },
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend:boolean = true;
  public lineChartType:string = 'line';

  //...

  private getTemperaturesToday(station:number, add:boolean, option:number):void {
    var func:any;

    option = +option;

    switch (option) {
      case 1:
        func = this._dataTemperatureService.GetStationTemperaturesInLastHour(station);
        break;

      case 2:
        func = this._dataTemperatureService.GetStationTemperaturesInLastXHours(station, 2);
        break;

      case 3:
        func = this._dataTemperatureService.GetStationTemperaturesInLastXHours(station, 4);
        break;

      case 4:
        func = this._dataTemperatureService.GetStationTemperaturesInLastXHours(station, 6);
        break;

      case 5:
        func = this._dataTemperatureService.GetStationTemperaturesInLastXHours(station, 12);
        break;

      case 6:
        func = this._dataTemperatureService.GetStationTemperaturesToday(station);
        break;

      case 7:
        func = this._dataTemperatureService.GetStationTemperaturesInLastDays(station, 7);
        break;

      default:
        func = this._dataTemperatureService.GetStationTemperaturesToday(station);
    }

    func.subscribe((data:Measurement[]) => this.updateChartData(data, 'Temperature', add),
        error => {
          console.log(error);
          this.errorMsg = error;
          this.lineChartData = [{data: [0], label: 'temp'}];
          this.lineChartLabels = ['any'];
        },
        () => {
        }
    );
  }

  private getHumidityToday(station:number, add:boolean, option:number):void {
    var func:any;

    option = +option;

    switch (option) {
      case 1:
        func = this._dataHumidityService.GetStationHumiditiesInLastHour(station);
        break;

      case 2:
        func = this._dataHumidityService.GetStationHumiditiesInLastXHours(station, 2);
        break;

      case 3:
        func = this._dataHumidityService.GetStationHumiditiesInLastXHours(station, 4);
        break;

      case 4:
        func = this._dataHumidityService.GetStationHumiditiesInLastXHours(station, 6);
        break;

      case 5:
        func = this._dataHumidityService.GetStationHumiditiesInLastXHours(station, 12);
        break;

      case 6:
        func = this._dataHumidityService.GetStationHumiditiesToday(station);
        break;

      case 7:
        func = this._dataHumidityService.GetStationHumiditiesInLastDays(station, 7);
        break;

      default:
        func = this._dataHumidityService.GetStationHumiditiesToday(station);
    }

    console.log("Adding Humidity")
    func.subscribe((data:Measurement[]) => this.updateChartData(data, 'Humidity', add),
            error => {
              console.log(error);
              this.errorMsg = error;
              this.lineChartData = [{data: [0], label: 'temp'}];
              this.lineChartLabels = ['any'];
            },
            () => {
            }
        );
  }

  private getHeatIndexToday(station:number, add:boolean, option:number):void {
    this._dataHumidityService
        .GetStationHumiditiesToday(station)
        .subscribe((data:Measurement[]) => this.updateChartData(data, 'Heat Index', add),
            error => {
              console.log(error);
              this.errorMsg = error;
              this.lineChartData = [{data: [0], label: 'temp'}];
              this.lineChartLabels = ['any'];
            },
            () => {
            }
        );
  }

  private getStationInformation(station:number):void {
    this._dataStationService
        .GetStation(station)
        .subscribe((data:Station) => this.stationData = data,
            error => {
              console.log(error)
              this.errorMsg = error;
              this.lineChartData = [{data: [0], label: 'temp'}];
              this.lineChartLabels = ['any'];
            },
            () => {
              console.log('Get data complete for station ' + station);
            }
        );
  }

  private static showTime(timeStamp:string):string {
    return timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ":" + timeStamp.substr(12, 2);
  }

  private updateChartData(chartData:Measurement[], description:string, add:boolean):void {
    let _lineChartLabels:Array<any> = new Array(chartData.length);
    let _lineChartData:any = {
      data: new Array(chartData.length),
      label: ' ' + description
    };

    for (let j = 0; j < chartData.length; j++) {
      _lineChartData.data[j] = chartData[j].value;
      _lineChartLabels[j] = DisplayChartComponent.showTime(chartData[j].timestamp);
    }

    this.lineChartLabels = _lineChartLabels;

    if (add) {
      this.lineChartData.push(_lineChartData);
    }
    else {
      this.lineChartData = [_lineChartData];
    }
  }

  // events
  public chartClicked(e:any):void {
    console.log(e);
  }

  public chartHovered(e:any):void {
    console.log("Hover");
    console.log(e);
  }
}
