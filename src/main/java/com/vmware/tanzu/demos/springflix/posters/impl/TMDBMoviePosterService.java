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

import com.vmware.tanzu.demos.springflix.posters.model.MoviePoster;
import com.vmware.tanzu.demos.springflix.posters.model.MoviePosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
class TMDBMoviePosterService implements MoviePosterService {
    private final Logger logger = LoggerFactory.getLogger(TMDBMoviePosterService.class);
    private final TMDBClient client;

    private final URI imagesBaseUrl;

    TMDBMoviePosterService(TMDBClient client, @Value("${app.tmdb.images.url}") String imagesBaseUrl) {
        this.client = client;
        this.imagesBaseUrl = URI.create(imagesBaseUrl);
    }

    @Override
    public Optional<MoviePoster> getMoviePoster(String movieId) {
        try {
            final var mp = client.getMoviePoster(movieId);
            if (mp.posterPath() == null) {
                return Optional.empty();
            }
            return Optional.of(new MoviePoster(movieId, UriComponentsBuilder.fromUri(imagesBaseUrl).path(mp.posterPath()).build().toUri()));
        } catch (Exception e) {
            logger.warn("Error while getting movie poster: {}", movieId, e);
            return Optional.empty();
        }
    }
}
