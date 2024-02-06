package io.castle.client.internal.json;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.castle.client.model.generated.ChangesetEntry;

import java.io.IOException;

public class ChangesetEntryTypeAdapter extends TypeAdapter<ChangesetEntry> {
    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      @SuppressWarnings("unchecked")
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != ChangesetEntry.class) {
          return null;
        }
        TypeAdapter<ChangesetEntry> delegate = (TypeAdapter<ChangesetEntry>) gson.getDelegateAdapter(this, type);
        return (TypeAdapter<T>) new ChangesetEntryTypeAdapter(delegate);
      }
    };
  
    private final TypeAdapter<ChangesetEntry> delegate;
  
    ChangesetEntryTypeAdapter(TypeAdapter<ChangesetEntry> delegate) {
      this.delegate = delegate;
    }
  
    @Override public void write(JsonWriter out, ChangesetEntry value) throws IOException {
      boolean serializeNulls = out.getSerializeNulls();
      out.setSerializeNulls(true);
      try {
        delegate.write(out, value);
      } finally {
        out.setSerializeNulls(serializeNulls);
      }
    }
  
    @Override public ChangesetEntry read(JsonReader in) throws IOException {
      return delegate.read(in);
    }
  }
