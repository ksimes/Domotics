import {NgModule} from '@angular/core';

import {DisplayAllStations} from './app.component';
import {BrowserModule} from '@angular/platform-browser';
import {DataSensorTypeService} from './services/sensortype.services';
import {DataStationService} from './services/station.services';
import {HttpModule} from '@angular/http';
import {DisplayStationComponent} from './DisplayStation/display-station.component';

@NgModule({
  declarations: [
    DisplayAllStations,
    DisplayStationComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
  ],
  providers: [DataSensorTypeService, DataStationService],
  bootstrap: [DisplayAllStations]
})
export class AppModule {
}
