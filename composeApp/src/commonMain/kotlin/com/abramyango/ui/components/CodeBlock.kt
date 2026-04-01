package com.abramyango.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.ui.theme.AppTheme

/**
 * Code Block - блок кода с подсветкой синтаксиса
 */
@Composable
fun CodeBlock(
    code: String,
    language: ProgrammingLanguage,
    modifier: Modifier = Modifier,
    showLineNumbers: Boolean = true,
    highlightedLine: Int? = null
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val shape = RoundedCornerShape(12.dp)
    
    val lines = code.lines()
    val highlightedCode = remember(code, language) {
        highlightSyntax(code, language, colors)
    }
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.codeBackground)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = shape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Line numbers
            if (showLineNumbers) {
                Column(
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    lines.forEachIndexed { index, _ ->
                        val lineNumber = index + 1
                        val isHighlighted = highlightedLine == lineNumber
                        
                        Text(
                            text = lineNumber.toString().padStart(2, ' '),
                            style = typography.codeBlock,
                            color = if (isHighlighted) {
                                colors.accentWarning
                            } else {
                                colors.textTertiary
                            }
                        )
                    }
                }
            }
            
            // Code content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                lines.forEachIndexed { index, line ->
                    val lineNumber = index + 1
                    val isHighlighted = highlightedLine == lineNumber
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isHighlighted) {
                                    Modifier.background(
                                        colors.accentWarning.copy(alpha = 0.15f)
                                    )
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        Text(
                            text = highlightLine(line, language, colors),
                            style = typography.codeBlock,
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Подсветка синтаксиса для всего кода
 */
private fun highlightSyntax(
    code: String,
    language: ProgrammingLanguage,
    colors: com.abramyango.ui.theme.AppColors
): AnnotatedString {
    return buildAnnotatedString {
        code.lines().forEach { line ->
            append(highlightLine(line, language, colors))
            append("\n")
        }
    }
}

/**
 * Подсветка синтаксиса для одной строки
 */
private fun highlightLine(
    line: String,
    language: ProgrammingLanguage,
    colors: com.abramyango.ui.theme.AppColors
): AnnotatedString {
    val keywords = getKeywords(language)
    
    return buildAnnotatedString {
        var currentIndex = 0
        val tokens = tokenize(line, keywords)
        
        tokens.forEach { token ->
            val color = when (token.type) {
                TokenType.KEYWORD -> colors.codeKeyword
                TokenType.STRING -> colors.codeString
                TokenType.NUMBER -> colors.codeNumber
                TokenType.COMMENT -> colors.codeComment
                TokenType.FUNCTION -> colors.codeFunction
                TokenType.VARIABLE -> colors.codeVariable
                TokenType.PLAIN -> colors.textPrimary
            }
            
            withStyle(SpanStyle(color = color)) {
                append(token.text)
            }
        }
    }
}

private enum class TokenType {
    KEYWORD, STRING, NUMBER, COMMENT, FUNCTION, VARIABLE, PLAIN
}

private data class Token(val text: String, val type: TokenType)

/**
 * Простая токенизация для подсветки
 */
private fun tokenize(line: String, keywords: Set<String>): List<Token> {
    val tokens = mutableListOf<Token>()
    var i = 0
    
    while (i < line.length) {
        // Comment check
        if (line.substring(i).startsWith("#") || line.substring(i).startsWith("//")) {
            tokens.add(Token(line.substring(i), TokenType.COMMENT))
            break
        }
        
        // String check (single and double quotes)
        if (line[i] == '"' || line[i] == '\'') {
            val quote = line[i]
            val start = i
            i++
            while (i < line.length && line[i] != quote) {
                if (line[i] == '\\' && i + 1 < line.length) i++
                i++
            }
            if (i < line.length) i++
            tokens.add(Token(line.substring(start, i), TokenType.STRING))
            continue
        }
        
        // Number check
        if (line[i].isDigit()) {
            val start = i
            while (i < line.length && (line[i].isDigit() || line[i] == '.')) i++
            tokens.add(Token(line.substring(start, i), TokenType.NUMBER))
            continue
        }
        
        // Word check (keywords, functions, variables)
        if (line[i].isLetter() || line[i] == '_') {
            val start = i
            while (i < line.length && (line[i].isLetterOrDigit() || line[i] == '_')) i++
            val word = line.substring(start, i)
            
            val type = when {
                word in keywords -> TokenType.KEYWORD
                i < line.length && line[i] == '(' -> TokenType.FUNCTION
                else -> TokenType.PLAIN
            }
            tokens.add(Token(word, type))
            continue
        }
        
        // Other characters
        tokens.add(Token(line[i].toString(), TokenType.PLAIN))
        i++
    }
    
    return tokens
}

/**
 * Ключевые слова для каждого языка
 */
private fun getKeywords(language: ProgrammingLanguage): Set<String> {
    return when (language) {
        ProgrammingLanguage.PYTHON -> setOf(
            "and", "as", "assert", "async", "await", "break", "class", "continue",
            "def", "del", "elif", "else", "except", "False", "finally", "for",
            "from", "global", "if", "import", "in", "is", "lambda", "None",
            "nonlocal", "not", "or", "pass", "raise", "return", "True", "try",
            "while", "with", "yield", "print", "input", "int", "float", "str",
            "bool", "list", "dict", "set", "tuple", "range", "len", "abs", "max",
            "min", "sum", "round", "type"
        )
        ProgrammingLanguage.JAVASCRIPT -> setOf(
            "async", "await", "break", "case", "catch", "class", "const",
            "continue", "debugger", "default", "delete", "do", "else", "export",
            "extends", "false", "finally", "for", "function", "if", "import",
            "in", "instanceof", "let", "new", "null", "return", "static",
            "super", "switch", "this", "throw", "true", "try", "typeof",
            "undefined", "var", "void", "while", "with", "yield", "console",
            "log", "Math", "parseInt", "parseFloat", "prompt", "alert"
        )
        ProgrammingLanguage.KOTLIN -> setOf(
            "abstract", "actual", "annotation", "as", "break", "by", "catch",
            "class", "companion", "const", "constructor", "continue", "crossinline",
            "data", "do", "else", "enum", "expect", "external", "false", "final",
            "finally", "for", "fun", "get", "if", "import", "in", "infix",
            "init", "inline", "inner", "interface", "internal", "is", "it",
            "lateinit", "noinline", "null", "object", "open", "operator", "out",
            "override", "package", "private", "protected", "public", "reified",
            "return", "sealed", "set", "super", "suspend", "this", "throw",
            "true", "try", "typealias", "typeof", "val", "var", "vararg",
            "when", "where", "while", "println", "print", "readLine", "toInt",
            "toDouble", "toString"
        )
        ProgrammingLanguage.CSHARP -> setOf(
            "abstract", "as", "base", "bool", "break", "byte", "case", "catch",
            "char", "checked", "class", "const", "continue", "decimal", "default",
            "delegate", "do", "double", "else", "enum", "event", "explicit",
            "extern", "false", "finally", "fixed", "float", "for", "foreach",
            "goto", "if", "implicit", "in", "int", "interface", "internal",
            "is", "lock", "long", "namespace", "new", "null", "object",
            "operator", "out", "override", "params", "private", "protected",
            "public", "readonly", "ref", "return", "sbyte", "sealed", "short",
            "sizeof", "stackalloc", "static", "string", "struct", "switch",
            "this", "throw", "true", "try", "typeof", "uint", "ulong",
            "unchecked", "unsafe", "ushort", "using", "var", "virtual", "void",
            "volatile", "while", "Console", "WriteLine", "ReadLine", "Parse",
            "Math", "Abs", "Sqrt"
        )
    }
}
