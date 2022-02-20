package jrain.immutable.identifiable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jrain.immutable.identifiable.Identifiable.WELL_FORMED;

class IdentifiableTest {

	@Nested
	@DisplayName("Test static methods")
	class TestStaticMethods{
		@ParameterizedTest
		@DisplayName("Test valid identifiers")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314","I:a76018f5-13fc-4f63-86e9-b7a4118bb314","Identifiable:a76018f5-13fc-4f63-86e9-b7a"})
		void testValid(String s) {
			assertEquals(WELL_FORMED.TRUE,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test empty")
		@ValueSource(strings={""})
		void testEmpty(String s) {
			assertEquals(WELL_FORMED.FALSE_EMPTY,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test separator only")
		@ValueSource(strings={":"})
		void testSeparatorOnly(String s) {
			assertEquals(WELL_FORMED.FALSE_SEPARATOR_ONLY,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test missing separator and unrecognizable UUID")
		@ValueSource(strings={"Identifiable;a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testMissingSeparatorInvalidUUID(String s) {
			assertEquals(WELL_FORMED.FALSE_NO_SEPARATOR_INVALID_UUID,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test missing separator with valid UUID")
		@ValueSource(strings={"a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testMissingSeparatorValidUUID(String s) {
			assertEquals(WELL_FORMED.FALSE_NO_SEPARATOR_UNKNOWN_OBJECT,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test multiple separators")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9:b7a4118bb314"})
		void testMultipleSeparators(String s) {
			assertEquals(WELL_FORMED.FALSE_MULTIPLE_SEPARATORS,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test missing object type")
		@ValueSource(strings={":a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testMissingObjectType(String s) {
			assertEquals(WELL_FORMED.FALSE_NO_OBJECT_TYPE,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test missing object type with invalid UUID")
		@ValueSource(strings={":a"})
		void testMissingObjectTypeInvalidUUID(String s) {
			assertEquals(WELL_FORMED.FALSE_NO_OBJECT_TYPE_INVALID_UUID,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test invalid UUID")
		@ValueSource(strings={"Identifiable:a"})
		void testInvalidUUID(String s) {
			assertEquals(WELL_FORMED.FALSE_INVALID_UUID,Identifiable.wellFormed(s).getKey(),s);
		}

		@ParameterizedTest
		@DisplayName("Test missing UUID")
		@ValueSource(strings={"Identifiable:"})
		void testMissingUUID(String s) {
			assertEquals(WELL_FORMED.FALSE_NO_UUID,Identifiable.wellFormed(s).getKey(),s);
		}

	}

	@Test
	@DisplayName("Test Inheritance")
	void testInheritance() {
		assertTrue(jrain.immutable.identifiable.interfaces.Identifiable.isIdentifiable(Qwerty.class));
		assertEquals("Qwerty",jrain.immutable.identifiable.interfaces.Identifiable.objectTypeOf(Qwerty.class));
		assertFalse(jrain.immutable.identifiable.interfaces.Identifiable.isIdentifiable(Integer.class),"Integer class does not extend Identifible");
		assertEquals("Integer",jrain.immutable.identifiable.interfaces.Identifiable.objectTypeOf(Integer.class));
		Qwerty qwe=new Qwerty();
		assertTrue(jrain.immutable.identifiable.interfaces.Identifiable.isIdentifiable(qwe));
		assertEquals("Qwerty",jrain.immutable.identifiable.interfaces.Identifiable.objectTypeOf(qwe));
		Integer i=13;
		assertFalse(jrain.immutable.identifiable.interfaces.Identifiable.isIdentifiable(i));
		assertEquals("Integer",jrain.immutable.identifiable.interfaces.Identifiable.objectTypeOf(i));
	}
	
	public static class Qwerty extends jrain.immutable.identifiable.Identifiable{
		
	}
	
}
