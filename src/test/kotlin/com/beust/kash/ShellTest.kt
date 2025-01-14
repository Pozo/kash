package com.beust.kash

import org.assertj.core.api.Assertions.assertThat
import org.jline.terminal.TerminalBuilder
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class ShellTest {
    private val shell = Shell(TerminalBuilder.builder().build())
    private val Shell.CommandResult.out get() = this.stdout?.replace("\r", "")

    @DataProvider
    fun simpleDp() = arrayOf(
            arrayOf("echo src", "src\n"),
            arrayOf("ls `echo src`", "main\ntest\n")
    )

    @Test(dataProvider = "simpleDp")
    fun simple(line: String, expected: String) {
        val result = shell.runLine(line, false)
        assertThat(result.out).isEqualTo(expected)
    }

    fun pipe() {
        val result = shell.runLine("echo a\nb\nc | wc", false)
        assertThat(result.stdout).startsWith("       1       3       6")
    }

    fun env() {
        val result = shell.runLine("echo \$A", false)
        assertThat(result.stdout).isNull()
        shell.runLine("A=B", false)
        val result2 = shell.runLine("echo \$A", false)
        assertThat(result2.out).isEqualTo("B\n")
    }
}