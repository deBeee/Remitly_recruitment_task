package com.app.model;

import java.util.List;

public record Statement(
        String Sid,
        String Effect,
        List<String> Action,
        String Resource
) {
}
