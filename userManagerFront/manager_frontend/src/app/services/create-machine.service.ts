import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {GlobalConstants} from "../global-constants";

@Injectable({
  providedIn: 'root'
})
export class CreateMachineService {



  constructor(private httpClient: HttpClient) {

  }

  newMachine(active: boolean, name: string) : Observable<any> {
    console.log("Active: " +active);
    let headers = new HttpHeaders()
      .set(
        'Content-Type', 'application/json; charset=utf-8')
      .set(
        'Authorization', `Bearer ${localStorage.getItem('token')}`
      );



    let body = {
      active: active,
      name: name
    };

    return this.httpClient.post(GlobalConstants.createMachineUrl, JSON.stringify(body), {
      headers: headers,
      observe: 'response'
    })


  }

}
