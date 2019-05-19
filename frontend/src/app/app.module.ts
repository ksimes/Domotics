import {NgModule} from '@angular/core';

import {DomoticsComponent} from './app.component';
import {BrowserModule} from '@angular/platform-browser';
import {DisplayAllStations} from './DisplayAllStations/display-all-stations.component';
import {OptionSelectComponent} from './OptionSelect/option-select.component';
import {AppRoutingModule} from './app-routing.module';
import {DisplayChartComponent} from './DisplayChartComponent/display-chart.component';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AddHeaderInterceptor} from "./services/interceptor.service";
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {AgGridModule} from 'ag-grid-angular';
import {DataHumidityService} from "./services/humidity.services";
import {DataHeatIndexService} from "./services/heatIndex.services";
import {DataTemperatureService} from "./services/temperature.services";
import {DataStationService} from "./services/station.services";
import {Configuration} from "./models/configuration";
import {WarningComponent} from './DisplayAllStations/components/warning/warning.component';
import {LinkToComponent} from './DisplayAllStations/components/link-to/link-to.component';
import {SensorCacheService} from "./services/sensorCache.services";

@NgModule({
  declarations: [
    DomoticsComponent,
    DisplayAllStations,
    OptionSelectComponent,
    DisplayChartComponent,
    PageNotFoundComponent,
    WarningComponent,
    LinkToComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    AgGridModule.withComponents([WarningComponent, LinkToComponent]),
    ChartsModule
  ],
  // providers: [],
  providers: [DataStationService, DataHeatIndexService, DataTemperatureService, DataHumidityService, SensorCacheService,
    Configuration,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AddHeaderInterceptor,
      multi: true,
    }],
  bootstrap: [DomoticsComponent]
})
export class DomoticsModule {
}
