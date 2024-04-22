package com.app.model;

import java.util.List;

public record PolicyDocument(
        String Version,
        List<Statement> Statement
) {
}
