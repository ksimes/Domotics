import {Component, Input, OnChanges, SimpleChange} from '@angular/core';
import {OnInit} from '@angular/core';
import {DataStationService} from '../services/station.services';
import {DataTemperatureService} from '../services/temperature.services';
import {DataHumidityService} from '../services/humidity.services';
import {Measurement} from '../models/Measurement';
import {Station} from '../models/Station';
import {Configuration} from '../models/configuration';
import {DisplayOptions} from '../models/DisplayOptions';
import Chart from 'chart.js';


@Component({
  selector: 'display-chart',
  providers: [DataStationService, DataTemperatureService, DataHumidityService, Configuration],
  templateUrl: './display-chart.component.html',
  styleUrls: ['./display-chart.component.css'],
})

export class DisplayChartComponent implements OnChanges, OnInit {
  @Input('config')
  config: DisplayOptions;

  station: number = 1;
  stationData: Station;
  errorMsg: string = '';
  displayChart: Chart = null;

  // lineChart
  public lineChartData: Array<any>;
  // public lineChartLabels: Array<any>;

  public lineChartOptions: any;
  public lineChartColours: Array<any>;
  public lineChartLegend: boolean;
  public lineChartType: string;

  constructor(private _dataStationService: DataStationService,
              private _dataTemperatureService: DataTemperatureService, private _dataHumidityService: DataHumidityService) {
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    // let log:string[] = [];
    // for (let propName in changes) {
    //   let changedProp = changes[propName];
    //   let from = JSON.stringify(changedProp.previousValue);
    //   let to = JSON.stringify(changedProp.currentValue);
    //   log.push(`${propName} changed from ${from} to ${to}`);
    // }
    // console.log(log.join(', '));
    this.clear();
    this.refresh(this.config);
  }

  ngOnInit(): void {
    this.globalClear();
  }

  public refresh(config: DisplayOptions) {
    this.errorMsg = '';
    this.station = config.stationId;
    this.getStationInformation(this.station);

    console.log('display Id  = ' + config.displayId);

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

  private globalClear(): void {
    this.lineChartOptions = {
      animation: false,
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        xAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: 'Time & Date'
          }
        }],
        yAxes: [{
          display: true,
          scaleLabel: {
            display: true,
            labelString: 'Temperature °C'
          }
        }]
      }
    };

    this.lineChartColours = [
      { // RegalRed(0xcc3366) 204, 51, 102
        backgroundColor: 'rgba(204,51,102,0.2)',
        borderColor: 'rgba(204,51,102,1)',
        pointBackgroundColor: 'rgba(204,51,102,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(204,51,102,1)'
      },
      { // LightBlue(0x009cce) 00, 156, 206
        backgroundColor: 'rgba(0,159,206,0.2)',
        borderColor: 'rgba(0,159,206,1)',
        pointBackgroundColor: 'rgba(0,159,206,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(0,159,206,0.8)'
      },
      { // MossGreen(0x008000) 0, 128, 0
        backgroundColor: 'rgba(0,128,0,0.2)',
        borderColor: 'rgba(0,128,0,1)',
        pointBackgroundColor: 'rgba(0,128,0,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(0,128,0,0.8)'
      }
    ];

    this.lineChartLegend = true;
    this.lineChartType = 'line';
  }

  private clear(): void {
    this.lineChartData = [{data: [0], label: 'temp'}];
    // this.lineChartLabels = [];
  }

  private updateChartData(chartData: Measurement[], description: string, add: boolean): void {
    let _lineChartLabels: Array<any> = new Array(chartData.length);

    let _lineChartData: any = {
      data: new Array(chartData.length),
      label: ' ' + description,
      backgroundColor: this.lineChartColours[0].backgroundColor,
      borderColor: this.lineChartColours[0].borderColor,
      pointBackgroundColor: this.lineChartColours[0].pointBackgroundColor,
      pointBorderColor: this.lineChartColours[0].pointBorderColor,
      pointHoverBackgroundColor: this.lineChartColours[0].pointHoverBackgroundColor,
      pointHoverBorderColor: this.lineChartColours[0].pointHoverBorderColor
    };

    for (let j = 0; j < chartData.length; j++) {
      _lineChartData.data[j] = chartData[j].value;
      _lineChartLabels[j] = DisplayChartComponent.showTime(chartData[j].timestamp);
    }

    // this.lineChartLabels = _lineChartLabels

    if (add) {
      this.lineChartData.push(_lineChartData);
    }
    else {
      this.lineChartData = [_lineChartData];
    }

    let ctx = document.getElementById('chart');

    if (this.displayChart != null) {
      this.displayChart.destroy();
    }

    this.displayChart = new Chart(ctx, {
      type: this.lineChartType,
      data: {
        labels: _lineChartLabels,
        datasets: this.lineChartData,
      },
      legend: this.lineChartLegend,
      options: this.lineChartOptions
    });

    // and render the chart
    this.displayChart.update();
  }


  //...

  private getTemperaturesToday(station: number, add: boolean, option: number): void {
    let func: any;

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

    func.subscribe((data: Measurement[]) => this.updateChartData(data, 'Temperature', add),
      error => {
        this.errorMsg = this.handleError(error);
        this.lineChartData = [{data: [0], label: 'temp'}];
        // this.lineChartLabels = ['any'];
      },
      () => {
      }
    );
  }

  private getHumidityToday(station: number, add: boolean, option: number): void {
    let func: any;

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

    console.log('Adding Humidity');
    func.subscribe((data: Measurement[]) => this.updateChartData(data, 'Humidity', add),
      error => {
        this.errorMsg = this.handleError(error);
        this.lineChartData = [{data: [0], label: 'temp'}];
        // this.lineChartLabels = [];
      },
      () => {
      }
    );
  }

  private getHeatIndexToday(station: number, add: boolean, option: number): void {
    this._dataHumidityService
      .GetStationHumiditiesToday(station)
      .subscribe((data: Measurement[]) => this.updateChartData(data, 'Heat Index', add),
        error => {
          this.errorMsg = this.handleError(error);
          this.lineChartData = [{data: [0], label: 'Heat Index'}];
          // this.lineChartLabels = [];
        },
        () => {
        }
      );
  }

  private getStationInformation(station: number): void {
    this._dataStationService
      .getStation(station)
      .subscribe((data: Station) => this.stationData = data,
        error => {
          this.errorMsg = this.handleError(error);
          this.lineChartData = [{data: [0], label: 'temp'}];
          // this.lineChartLabels = ['any'];
        },
        () => {
          console.log('Get data complete for station ' + station);
        }
      );
  }

  // Show the hours minutes and seconds formatted in the label, but when you cross a day then also
  // show the century, year, month and day.
  private static showTime(timeStamp: string): string {
    let result = '';
    let hrs = +timeStamp.substr(8, 2);
    let mins = timeStamp.substr(10, 2);
    let secs = timeStamp.substr(12, 2);

    // Is this near midnight, i.e. within 9 minutes of midnight.
    if ((hrs == 0) && (mins.startsWith('0'))) {
      result = timeStamp.substr(0, 4) + '-' + timeStamp.substr(4, 2) + '-' + timeStamp.substr(6, 2) +
        '  ' + timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ':' + timeStamp.substr(12, 2);
    }
    else {
      result = timeStamp.substr(8, 2) + ':' + timeStamp.substr(10, 2) + ':' + timeStamp.substr(12, 2);
    }

    return result;
  }

  // events
  public chartClicked(e: any): void {
    // console.log(e);
  }

  public chartHovered(e: any): void {
    console.log('Hover');
    console.log(e.active[0]._index);
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
