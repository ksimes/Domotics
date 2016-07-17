import {Injectable} from '@angular/core';

@Injectable()
export class Utilities {

  public static pad(num:number):string {
    return '' + (num <= 9 ? '0' + num : num);
  }

  public static getFormattedTodayDate():string {
    var d = new Date();
    var m = d.getMonth();
    m++;
    var day = d.getDate();
    return ('' + d.getFullYear() + Utilities.pad(m) + Utilities.pad(day) + Utilities.pad(d.getHours()) + Utilities.pad(d.getMinutes()) + Utilities.pad(d.getSeconds()));
  }
}