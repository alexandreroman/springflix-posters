/*
 * Copyright (c) 2023 VMware, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vmware.tanzu.demos.springflix.posters.impl;

import com.vmware.tanzu.demos.springflix.posters.model.MoviePosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
class MoviePosterController {
    private final Logger logger = LoggerFactory.getLogger(MoviePosterController.class);
    private final MoviePosterService moviePosterService;

    MoviePosterController(MoviePosterService moviePosterService) {
        this.moviePosterService = moviePosterService;
    }

    @GetMapping(value = "/api/v1/posters/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getMoviePoster(@PathVariable(value = "movieId") String movieId) {
        logger.info("Looking up movie poster: {}", movieId);
        final var mOpt = moviePosterService.getMoviePoster(movieId);
        return mOpt.map(m -> ResponseEntity.status(HttpStatus.FOUND)
                        .cacheControl(CacheControl.maxAge(Duration.ofMinutes(10)))
                        .location(m.imageUri()).build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
