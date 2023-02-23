import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {GlobalConstants} from "../global-constants";


@Injectable({
  providedIn: 'root'
})
export class NewUserService {

  constructor(private httpClient: HttpClient) { }

  newUser(name: string, surname: string, email: string, password: string, permissions: string[]): Observable<any> {
    let headers = new HttpHeaders()
      .set(
        'Content-Type', 'application/json; charset=utf-8')
      .set(
        'Authorization', `Bearer ${localStorage.getItem('token')}`
      )

    let body = {
      name: name,
      surname: surname,
      username: email,
      password: password,
      permissions: permissions
    }

    return this.httpClient.post(GlobalConstants.newUserUrl, JSON.stringify(body), {
      headers: headers,
      observe: 'response'
    })
  }
}
