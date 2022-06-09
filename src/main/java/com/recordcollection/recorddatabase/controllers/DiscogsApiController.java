package com.recordcollection.recorddatabase.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/discogs")
@PreAuthorize("hasRole('ADMIN')")
public class DiscogsApiController {


}
