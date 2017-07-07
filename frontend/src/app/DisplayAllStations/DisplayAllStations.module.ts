import {NgModule} from '@angular/core';

import {DisplayAllStations} from './DisplayAllStations.component';
import {BrowserModule} from '@angular/platform-browser';
import {DataSensorTypeService} from '../services/sensortype.services';
import {DataStationService} from '../services/station.services';
import {DisplayStationModule} from '../../../../temp_public/public/src/display-station.module';
import {HttpModule} from '@angular/http';

@NgModule({
  declarations: [
    DisplayAllStations
  ],
  imports: [
    BrowserModule,
    HttpModule,
    DisplayStationModule
  ],
  providers: [DataSensorTypeService, DataStationService],
  bootstrap: [DisplayAllStations]
})
export class DisplayAllStationsModule {
}
