package com.kumofactory.cloud;

import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import com.kumofactory.cloud.auth.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {
		private final JwtTokenProvider provider;

		@GetMapping("/")
		public String checkHealth() {
				return "success";
		}

		@GetMapping("/token")
		public String createMockTest() {
				TokenDto tokenDto = provider.create("11");
				return tokenDto.getAccessToken();
		}
}
