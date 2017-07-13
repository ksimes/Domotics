import {NgModule} from '@angular/core';

import {DomoticsComponent} from './app.component';
import {BrowserModule} from '@angular/platform-browser';
import {HttpModule} from '@angular/http';
import {DisplayStationComponent} from './DisplayStation/display-station.component';
import {DisplayAllStations} from './DisplayAllStations/display-all-stations.component';
import {OptionSelectComponent} from './OptionSelect/option-select.component';
import {AppRoutingModule} from './app-routing.module';
import {DisplayChartComponent} from './DisplayChartComponent/display-chart.component';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    DomoticsComponent,
    DisplayAllStations,
    DisplayStationComponent,
    OptionSelectComponent,
    DisplayChartComponent
   ],
  imports: [
    BrowserModule,
    HttpModule,
    FormsModule,
    AppRoutingModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [DomoticsComponent]
})
export class DomoticsModule { }
