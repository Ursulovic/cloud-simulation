import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {GlobalConstants} from "../global-constants";

@Injectable({
  providedIn: 'root'
})
export class ErrorMessageServiceService {

  constructor(private httpClient: HttpClient) { }

  getErrors() : Observable<any> {
    let headers = new HttpHeaders()
      .set(
        'Content-Type', 'application/json; charset=utf-8')
      .set(
        'Authorization', `Bearer ${localStorage.getItem('token')}`
      );

    return this.httpClient.get(GlobalConstants.getErrorsUrl, {
      headers : headers,
      observe: 'response'
    })

  }
}
