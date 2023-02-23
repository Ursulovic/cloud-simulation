import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {GlobalConstants} from "../global-constants";
import {Observable} from "rxjs";
import {LoginResponse} from "../model";


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  email: string;
  password: string;
  constructor(private httpClient: HttpClient) {
    this.email = '';
    this.password = '';
  }


  login(email: string, password: string) : Observable<any> {
    let body = JSON.stringify({
      username: email,
      password: password
    })

    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json; charset=utf-8');

    return this.httpClient.post<LoginResponse>(GlobalConstants.loginUrl , body, {
      observe: 'response',
      headers: headers});
  }

  setToken(token: string) {
    localStorage.setItem("token", token);
  }



}
