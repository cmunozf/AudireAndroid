Librerías necesarias para su funcionamiento:

```	
- httpclient
- Se implementó la autenticación con Google usando la librería de Firebase pero esta se comentó en el código ya que no aportaba una mejora en el core de la aplicación.
```

Como se está usando un servidor local se debe cambiar parte del código de la aplicación, ya que al momento de desplegar el BackEnd la dirección IP de este servidor cambia.

```
- Se debe tener instalado Android Studio.
[Android Studio](https://developer.android.com/studio/index.html?hl=es-419)
- Se debe clonar el repositorio Audire Android. 
[Github Audire Andoid](https://github.com/cmunozf/AudireAndroid)
- Se abre desde Android Studio el proyecto y se entra a la clase Datos.java , en esta de debe modificar en la variable url la nueva ip en la que está el BackEnd.
- Se hace el Build de la aplicación y se instala el apk generado.
```