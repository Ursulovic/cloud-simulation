import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {GlobalConstants} from "../global-constants";
import {Observable} from "rxjs";
import {User, UserPermission} from "../model";


@Injectable({
  providedIn: 'root'
})
export class UserListService {


  constructor(private httpClient: HttpClient) { }


  getAllUsers(): Observable<User[]> {

    let headers = new HttpHeaders()
      .set(
      'Content-Type', 'application/json; charset=utf-8')
      .set(
        'Authorization', `Bearer ${localStorage.getItem('token')}`
      )


    return this.httpClient.get<User[]>(GlobalConstants.userListUrl, {headers})
  }

  deleteUser(id: number): Observable<any> {
    let headers = new HttpHeaders()
      .set(
        'Content-Type', 'application/json; charset=utf-8')
      .set(
        'Authorization', `Bearer ${localStorage.getItem('token')}`
      )


    return this.httpClient.delete(`http://localhost:8080/users/delete/${id}`, {
      observe: 'response',
      headers: headers
    })
  }

}
