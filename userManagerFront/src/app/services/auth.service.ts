import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment } from '../../environments/environment';
import {LoginResponse} from "../model";

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  token: string;

  constructor(private httpClient: HttpClient) {
    this.token = '';
  }


  login(email: string, password: string) {
    const jwt = this.httpClient.post<LoginResponse>(environment.backendUrl, {
      username: email,
      password: password
    })
    console.log(jwt);
  }

  setToken(token: string) {
    localStorage.setItem("token", token);
  }

  removeToken() {
    localStorage.removeItem("token");
  }


}
