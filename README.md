# Compresor Huffman en Java

Este proyecto implementa un programa de compresión basado en el algoritmo de Huffman en Java. El programa permite comprimir y descomprimir archivos utilizando una estructura de árbol de Huffman para la codificación y decodificación.

## Características

- Comprime archivos utilizando el algoritmo de Huffman.
- Descomprime archivos previamente comprimidos.
- Mapea códigos de compresión para cada carácter.

## Cómo Utilizar el Programa

1. Clona el repositorio o descarga el código fuente en tu sistema.

2. Abre la clase principal `Compresor.java` en tu entorno de desarrollo Java.

3. Configura el directorio de salida (`outDir`) según tus necesidades:

   ```java
   compresor.setOutDir("directorio-de-salida");
  
4. Comprime un archivo.
   ```java
   compresor.cargarComprimir(new File("archivo-a-comprimir.txt"));
   compresor.comprimir();
   
5. Descomprime un archivo previamente comoprimido.
   ```java
   compresor.cargarDescomprimir(new File("archivo-comprimido.zap"));
   compresor.descomprimir();

## Estructura del Proyecto

- `Compresor.java`: Clase principal que maneja la compresión y descompresión de archivos.
- `ArbolH.java`: Implementación del árbol de Huffman.
- `NodoB.java`: Implementación de nodos binarios para el árbol de Huffman.

## Notas Importantes

- Asegúrate de configurar correctamente los directorios de entrada y salida antes de comprimir o descomprimir archivos.
- El programa utiliza una estrategia de compresión basada en el algoritmo de Huffman, lo que puede reducir significativamente el tamaño de los archivos.
