import {Component} from '@angular/core';

@Component({
  selector: 'app-warning',
  templateUrl: './warning.component.html',
  styleUrls: ['./warning.component.css']
})
export class WarningComponent {
  timestamp: string;

  agInit(params: any): void {
    this.timestamp = params.value;

    // if(this.timestamp == 'More than 30 minutes ago') {
    //   .id.class = 'warning'
    // }
  }
}
