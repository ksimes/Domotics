import {Component} from '@angular/core';
import {DataStationService} from './services/station.services';
import {Configuration} from './configuration';
import {DisplayOptions} from './models/DisplayOptions';
import {Option} from './models/Option';
import {Station} from './models/Station';
import {DataSensorTypeService} from './services/sensortype.services';
import {SensorType} from './models/SensorType';

@Component({
  selector: 'display-all-stations',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [Configuration, DataStationService, DataSensorTypeService],
})

export class DisplayAllStations {
  errorMsg: string = '';
  timeStamp: string = '';
  config: DisplayOptions;
  stationData: Station[];
  sensorData: SensorType[];
  options: Option[];
  title = 'Domotics';

  constructor(private _dataStationService: DataStationService, private _dataSensorService: DataSensorTypeService, public _configuration: Configuration) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
    this.refresh();
  }

  public refresh(): void {
    this.errorMsg = '';
    this.getSensorInformation();
    this.getStationInformation();
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
    this._dataStationService
      .GetAllStations()
      .subscribe((data: Station[]) => this.stationData = data,
        error => console.log(error),
        () => {
          console.log('Get all station data complete');
        }
      );
  }

  public getSensor(selectedSensorType: number): String {

    if (this.sensorData) {

      return this.sensorData[selectedSensorType].name;
    }

    return null;
  }


  public filteredByType(selectedSensorType: number): Station[] {
    if (this.stationData && this.stationData.length > 1) {

      return this.stationData.filter((station) =>
        station.sensorType == selectedSensorType
      );
    }

    return this.stationData;
  }
}
