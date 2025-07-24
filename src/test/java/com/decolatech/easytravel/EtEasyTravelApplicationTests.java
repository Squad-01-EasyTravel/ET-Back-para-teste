package com.decolatech.easytravel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EtEasyTravelApplicationTests {

	@Test
	void contextLoads() {
		// Teste para verificar se o contexto da aplicação carrega corretamente
		// Este teste garante que todas as configurações e beans estão corretos
	}

	@Test
	void applicationStarts() {
		// Teste adicional para verificar se a aplicação inicializa sem erros
		// Importante para detectar problemas de configuração
	}
}
