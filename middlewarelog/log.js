export async function Log(stack, level, pkg, message) {
  try {
    const token =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJyYWphZGhhbnVzaGdkbkBnbWFpbC5jb20iLCJleHAiOjE3NTMwODQ4OTgsImlhdCI6MTc1MzA4Mzk5OCwiaXNzIjoiQWZmb3JkIE1lZGljYWwgVGVjaG5vbG9naWVzIFByaXZhdGUgTGltaXRlZCIsImp0aSI6IjdmZWU2NGQ3LTExNmYtNDk1YS04NTI3LWEwODY1N2FiNjNjMiIsImxvY2FsZSI6ImVuLUlOIiwibmFtZSI6InIuZGhhbnVzaCIsInN1YiI6ImI2YTJiMDA4LWRmOGQtNGMzZi05NDg3LTliMTE2Mjg3N2FkNyJ9LCJlbWFpbCI6InJhamFkaGFudXNoZ2RuQGdtYWlsLmNvbSIsIm5hbWUiOiJyLmRoYW51c2giLCJyb2xsTm8iOiIyMmhyMWEwNTg4IiwiYWNjZXNzQ29kZSI6IkVFSHlUQyIsImNsaWVudElEIjoiYjZhMmIwMDgtZGY4ZC00YzNmLTk0ODctOWIxMTYyODc3YWQ3IiwiY2xpZW50U2VjcmV0IjoiY0pQR1JGSE1qVkhwYWdKVCJ9.aLCAYeBbb338ILJ5nH2mn0ItbiI-p1Onm88QizKPxPE";

    await fetch("http://20.244.56.144/evaluation-service/logs", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({
        stack,
        level,
        package: pkg,
        message
      }),
    });
  } catch (_) {
    
  }
}