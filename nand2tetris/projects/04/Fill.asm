// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.
// pseudocode
// while(1) {
//     if(KBD == 0) {
//         for () {
//         
//         }
//     } else {
//         for () {
//         }
//     }
// }
(LOOP)
    @i  // i=0
    M=0
    @KBD
    D=M
    @WHITE
    D;JEQ
(BLACK)
    
(LOOP1)
    @i  // if (i - 8192 == 0) goto LOOP
    D=M
    @8192
    D=D-A
    @LOOP
    D;JEQ
    
    @SCREEN // black the screen
    D=A     // D=screen
    @addr   // RAM[addr]=screen
    M=D
    @i      // D=i
    D=M
    @addr   // RAM[addr]+i = new_address
    M=D+M
    A=M     // A=new_address
    M=-1    // black

    @i  // i++
    M=M+1
    
    @LOOP1  // loop
    0;JMP
    
(WHITE)

(LOOP2)
    @i  // if (i == 8192) goto LOOP
    D=M
    @8192
    D=D-A
    @LOOP
    D;JEQ
    
    @SCREEN // black the screen
    D=A
    @addr
    M=D
    @i
    D=M
    @addr
    M=D+M
    A=M
    M=0    

    @i  // i++
    M=M+1
    
    @LOOP2  // loop
    0;JMP
