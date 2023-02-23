import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {HomeComponent} from "./components/home/home.component";
import {UserListComponent} from "./components/user-list/user-list.component";



const routes: Routes = [
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "list",
    component: UserListComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes),
    HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
