// package io.castle.client.deserializers;

// import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.DeserializationContext;
// import com.fasterxml.jackson.databind.JsonDeserializer;
// import io.castle.client.objects.Event;

// import java.io.IOException;

// public class EventNameDeserializer extends JsonDeserializer<Event.EventName> {
//     @Override
//     public Event.EventName deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
// 	return Event.EventName.fromName(jp.readValueAs(String.class));
//     }
// }
