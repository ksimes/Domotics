import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";

@Injectable()
export class AddHeaderInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // let clonedRequest: HttpRequest<any>;

    // console.log('Access-Control-Request-Original: ' + req.headers.get('Access-Control-Request-Headers'));
    // console.log('Cache-Control-Original: ' + req.headers.get('Cache-Control'));
    // console.log('Access-Control-Allow-Origin-Original: ' + req.headers.get('Access-Control-Allow-Origin'));

    // Clone the request to add the new header
    // const clonedRequest = req.clone({ headers: req.headers.set('Cache-Control', 'no-cache, no-store, must-revalidate')});
    // const clonedRequest = req.clone({ headers: req.headers.append('Cache-Control', 'no-cache, no-store, must-revalidate')});
    // const clonedRequest = req.clone({ headers: req.headers.append('Access-Control-Allow-Origin', '*')});

    // const clonedRequest = req.clone({setHeaders: {Authorization: `Bearer Simon`}});
    const clonedRequest = req.clone();


    // console.log('Cache-Control-Cloned: ' + clonedRequest.headers.get('Cache-Control'));
    // console.log('Access-Control-Request-Cloned: ' + clonedRequest.headers.get('Access-Control-Request-Headers'));
    // console.log('Access-Control-Allow-Origin-Cloned: ' + clonedRequest.headers.get('Access-Control-Allow-Cloned'));

    // Pass the cloned request instead of the original request to the next handle
    return next.handle(clonedRequest);
  }
}
