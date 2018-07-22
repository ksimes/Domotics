import {NgModule} from '@angular/core';

import {DomoticsComponent} from './app.component';
import {BrowserModule} from '@angular/platform-browser';
import {DisplayStationComponent} from './DisplayStation/display-station.component';
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
import {DegreesComponent} from './DisplayAllStations/components/degrees/degrees.component';

@NgModule({
  declarations: [
    DomoticsComponent,
    DisplayAllStations,
    DisplayStationComponent,
    OptionSelectComponent,
    DisplayChartComponent,
    PageNotFoundComponent,
    WarningComponent,
    LinkToComponent,
    DegreesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    AgGridModule.withComponents([WarningComponent]),
    ChartsModule
  ],
  // providers: [],
  providers: [DataStationService, DataHeatIndexService, DataTemperatureService, DataHumidityService,
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
