import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {OptionSelectComponent} from './OptionSelect/option-select.component';
import {DisplayAllStations} from './DisplayAllStations/display-all-stations.component';

const routes: Routes = [
  { path: '', redirectTo: '/domotics', pathMatch: 'full' },
  { path: 'domotics',  component: DisplayAllStations },
  { path: 'options/:id',  component: OptionSelectComponent },
//  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
