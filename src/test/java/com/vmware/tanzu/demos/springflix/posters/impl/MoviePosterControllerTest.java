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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
class MoviePosterControllerTest {
    @Autowired
    private TestRestTemplate client;

    @Test
    void testGetMoviePoster() {
        stubFor(get(urlEqualTo("/3/movie/299054"))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                  "adult": false,
                                  "backdrop_path": "/rMvPXy8PUjj1o8o1pzgQbdNCsvj.jpg",
                                  "belongs_to_collection": {
                                    "id": 126125,
                                    "name": "The Expendables Collection",
                                    "poster_path": "/zwnCtNpHa6TFI1z20LPFgdY2Qks.jpg",
                                    "backdrop_path": "/lA7oDSt6LkyDqtbFGbyuG0afmTI.jpg"
                                  },
                                  "budget": 100000000,
                                  "genres": [
                                    {
                                      "id": 28,
                                      "name": "Action"
                                    },
                                    {
                                      "id": 12,
                                      "name": "Adventure"
                                    },
                                    {
                                      "id": 53,
                                      "name": "Thriller"
                                    }
                                  ],
                                  "homepage": "https://expendables.movie/",
                                  "id": 299054,
                                  "imdb_id": "tt3291150",
                                  "original_language": "en",
                                  "original_title": "Expend4bles",
                                  "overview": "Armed with every weapon they can get their hands on and the skills to use them, The Expendables are the world’s last line of defense and the team that gets called when all other options are off the table. But new team members with new styles and tactics are going to give “new blood” a whole new meaning.",
                                  "popularity": 349.616,
                                  "poster_path": "/nbrqj9q8WubD3QkYm7n3GhjN7kE.jpg",
                                  "production_companies": [
                                    {
                                      "id": 1020,
                                      "logo_path": "/kuUIHNwMec4dwOLghDhhZJzHZTd.png",
                                      "name": "Millennium Media",
                                      "origin_country": "US"
                                    },
                                    {
                                      "id": 48738,
                                      "logo_path": null,
                                      "name": "Campbell Grobman Films",
                                      "origin_country": "US"
                                    },
                                    {
                                      "id": 1632,
                                      "logo_path": "/cisLn1YAUuptXVBa0xjq7ST9cH0.png",
                                      "name": "Lionsgate",
                                      "origin_country": "US"
                                    }
                                  ],
                                  "production_countries": [
                                    {
                                      "iso_3166_1": "US",
                                      "name": "United States of America"
                                    }
                                  ],
                                  "release_date": "2023-09-15",
                                  "revenue": 19400000,
                                  "runtime": 103,
                                  "spoken_languages": [
                                    {
                                      "english_name": "English",
                                      "iso_639_1": "en",
                                      "name": "English"
                                    }
                                  ],
                                  "status": "Released",
                                  "tagline": "They'll die when they're dead.",
                                  "title": "Expend4bles",
                                  "video": false,
                                  "vote_average": 6.598,
                                  "vote_count": 92
                                }
                                """)));

        final var mp = new MoviePoster("299054", URI.create("https://image.tmdb.org/t/p/w500/nbrqj9q8WubD3QkYm7n3GhjN7kE.jpg"));

        final var resp = client.getForEntity("/api/v1/posters/299054", MoviePoster.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(resp.getHeaders().getLocation()).isEqualTo(mp.imageUri());
    }
}
