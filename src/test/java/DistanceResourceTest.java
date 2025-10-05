// Código do arquivo DistanceResourceTest.java atualizado para usar @ParameterizedTest na linha 165

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DistanceResourceTest {
    // ... outras partes do código ...

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testDistanceCalculation(Object input, Object expected) {
        // lógica do teste
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(...), // Exemplos de argumentos
            // Adicione mais casos conforme necessário
        );
    }

    // ... outras partes do código ...
}