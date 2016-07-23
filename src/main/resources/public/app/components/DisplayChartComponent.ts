import {Component} from "@angular/core";
import {CORE_DIRECTIVES, NgClass, FORM_DIRECTIVES} from "@angular/common";
import {CHART_DIRECTIVES} from "ng2-charts";

import {DataStationService} from "../services/station.services";
import {DataTemperatureService} from "../services/temperature.services";
import {DataHumidityService} from "../services/humidity.services";

import {Measurement} from "../models/Measurement.ts";
import {Station} from "../models/Station.ts";
import {Configuration} from "../configuration";

@Component({
  selector: 'display-chart-component',
  providers: [DataStationService, DataTemperatureService, DataHumidityService, Configuration],
  styles: [`
    .chart {
      display: block;
    }`
  ],
  templateUrl: 'app/templates/DisplayChart.Component.html',
  directives: [CHART_DIRECTIVES, NgClass, CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class DisplayChartComponent {
  station:number = 1;
  stationData:Station;

  constructor(private _dataStationService:DataStationService, private _dataTemperatureService:DataTemperatureService, private _dataHumidityService:DataHumidityService) {
    this.getStationInformation(this.station);
    this.getTemperaturesToday(this.station, false);
    this.getHumidityToday(this.station, true);
  }

  // lineChart
  public lineChartData:Array<any> = [ { data: [0], label: 'temp' } ];
  public lineChartLabels:Array<any> = ['any'];

  public lineChartOptions:any = {
    animation: false,
    responsive: true
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

  private getTemperaturesToday(station:number, add:boolean):void {
    this._dataTemperatureService
        .GetStationTemperaturesToday(station)
        .subscribe((data:Measurement[]) => this.updateChartData(data, 'Temperature', add),
            error => console.log(error),
            () => {
            }
        );
  }

  private getHumidityToday(station:number, add:boolean):void {
    this._dataHumidityService
        .GetStationHumiditiesToday(station)
        .subscribe((data:Measurement[]) => this.updateChartData(data, 'Humidity', add),
            error => console.log(error),
            () => {
            }
        );
  }

  private getStationInformation(station:number):void {
    this._dataStationService
        .GetStation(station)
        .subscribe((data:Station) => this.stationData = data,
            error => console.log(error),
            () => {
              console.log('Get station data complete');
            }
        );
  }

  private static showTime(timeStamp:string):string {
    return timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ":" + timeStamp.substr(12, 2);
  }

  private updateChartData(chartData:Measurement[], description:string, add:boolean):void {
    let _lineChartLabels:Array<any> = new Array(chartData.length);
    let _lineChartData: any = {
      data: new Array(chartData.length),
      label: ' ' + description + ' report for Station ' + this.stationData.id + ' : ' + this.stationData.name
    };

    for (let j = 0; j < chartData.length; j++) {
      _lineChartData.data[j] = chartData[j].value;
      _lineChartLabels[j] = DisplayChartComponent.showTime(chartData[j].timestamp);
    }

    this.lineChartLabels = _lineChartLabels;

    if(add) {
      this.lineChartData.push(_lineChartData);
    }
    else {
      this.lineChartData = [ _lineChartData ];
    }
  }

  // events
  public chartClicked(e:any):void {
    console.log(e);
  }

  public chartHovered(e:any):void {
    console.log(e);
  }
}
