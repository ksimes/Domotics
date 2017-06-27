import {Component} from "@angular/core";
import {DataStationService} from "../services/station.services";
import {Configuration} from "../configuration";
import {DisplayOptions} from "../models/DisplayOptions";
import {Option} from "../models/Option";
import {Station} from "../models/Station";
import {DisplayStationComponent} from "./DisplayStationComponent";
import {DisplayChartComponent} from "./DisplayChartComponent";
import {DataSensorTypeService} from "../services/sensortype.services";
import {SensorType} from "../models/SensorType";

@Component({
  selector: 'display-all-stations-component',
  providers: [Configuration, DataStationService, DataSensorTypeService],
  templateUrl: 'app/templates/DisplayAllStations.Component.html',
  directives: [DisplayChartComponent, DisplayStationComponent]
})

export class DisplayAllStations {
  errorMsg: string = '';
  timeStamp: string = '';
  config: DisplayOptions;
  stationData: Station[];
  sensorData: SensorType[];
  options: Option[];

  constructor(private _dataStationService: DataStationService, private _dataSensorService: DataSensorTypeService, private _configuration: Configuration) {
    this.options = _configuration.display;
    this.config = _configuration.currentState;
    this.refresh();
  }

  private refresh(): void {
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

  public getSensor(selectedSensorType: number) : String {

    if (this.sensorData) {

      return this.sensorData[selectedSensorType].name;
    }

    return null;
  }


  public filteredByType(selectedSensorType: number) : Station[] {
    if (this.stationData && this.stationData.length > 1) {

      return this.stationData.filter((station) =>
          station.sensorType == selectedSensorType
      );
    }

    return this.stationData;
  }
}
