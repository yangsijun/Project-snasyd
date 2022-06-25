# Project-snasyd
AI for Connect6 (2022 Handong SW Festival)

------------------

# JAVA API for CONNSIX

## About
This API is for Connect Six programs developed with the Java programming language. Its functions connect and communicate with the single mode server.
<br>
�� API�� �ڹ� ���α׷��� ���� ������ ���� AI�� ���α׷����� ���� API�̴�. ��� method���� �̱� ��� ������ �����ϰ� �����ϱ� ���� ��ɵ��� �����Ǿ��ִ�.

## Coordinate System
The coordinate is consisted with an alphabet character and a number. The columns are notated with `A` ~ `T` with `I` not included. The rows are notated with `01` ~ `19`. The one digit numbers may or may not have a leading 0. To express more than one coordinates, the coordinates should be separated with `:`.
<br>
�ٵ����� ��ǥ�� alphabet character �ϳ��� ���ڷ� ǥ���Ѵ�. �ٵ����� �������� alphabet `A` ���� `T` �� ǥ���ϰ� �̶� `I`�� ���Ե��� �ʴ´�.
�������� ���� `01` ~ `19`�� ǥ���ϸ� 10 ������ ���ڵ��� ���� �ڸ��� 0���� ǥ���ص� ���ص��ȴ�.
�ϳ��� ��ǥ�� �� �� ������ ��ģ String���� ǥ���ؾ��Ѵ�.
���� ��ǥ���� ǥ���ϱ� ���ؼ��� ��ǥ�� ���̿� `:`�� �־� �ϳ��� String���� �������Ѵ�.
* Ex) `A01`, `A1`, `B03`, `B3`, `J12`, `A01:E13`, `E11:J18:K10:T19`
<br>
`Strict notation`: coorinate with leading 0.
* Ex) `A01`, `C04`
<br>
`Extended notation`: coorinate without leading 0.
* Ex) `A1`, `C4`
<br>
<br>
Below is an image that explains the coordinate system.
<br>
���� �̹����� �ռ� ������ ��ǥ �ý����� ��Ÿ�� ���̴�.
<br>
<img src="https://user-images.githubusercontent.com/54518241/137690844-2664cb7a-2bed-40ca-bdd5-e8fa87df5b42.png" alt="coordinate system" width="500"/>

## Methods
1. ***public ConnectSix(String ip, int port, String color)***   
Creates an instance of the class ConnectSix and connects to the single mode server.
When success, the field `redStones` will contain the positions of the red stones as a String type.
The positions of the red stones will follow the `strict notation` explained above.
On connection failure, the constructor will throw ConnSixException.
If there is no red stones to begin with, the field `redStones` will contain the null value.
The user must make a instance of the class using this constructor in order to use the single mode server and other methods.   
ConnectSix class�� ��ü�� ������ִ� ���ÿ� �̱� ��� ������ �������ش�. ��ü�� �����ϴ� �Ϳ� �����ϸ� `redStones`��� field �ȿ� ���ڿ��ε� ������ ��ǥ�� �����Ѵ�.
�̶� ������ ��ǥ�� ������ ������ `strict notation`�� ������. ��ü�� �����ϴ� ���� �������� ��� ConnSixException�� ������.
������ ���� ���, `redStones`���� null ���� ����.
����ڴ� ���� ���� �� constructor�� �̿��ؼ� class�� ��ü�� �����߸� �̱� ��� ������ �����ϰ� �ٸ� method���� ����� �� �ִ�.
<br><br>
__Parameter__
    - `ip`   
    String type that contains the ip information.   
    ip ������ ������ �ִ� ���ڿ�.   
        - Ex) "127.0.0.1"
    - `port`   
    Integer type that contains the port number information.   
    port ������ ������ �ִ� ����.
        - Ex) 8080
    - `color`   
    String type that contains the color of the stone that the client will be using.   
    ����ڰ� ����� ���� �� ������ ������ �ִ� ���ڿ�.
        - `White` `Black`
       
    __Throws__
    - `ConnSixException`   
    Throws an exception that happens when the network connection fail.
    Connection failure can happen because of ip, port information error, underlying protocol error and IOException related to socket creation.   
    ��Ʈ��ũ ������ �����ϸ� `ConnSixException` ���ܸ� ������. ���� ���д� �߸��� ip, port ����, �������� ���� �׸��� socket ���� ���� IOException ������ �߻��� �� �ִ�.
   
2. ***public String letsConnect(String ip, int port, String color)***   
Connects to the single mode server and gets the red stones' positions from the single mode server.
This function will be called from the constructor method.
Therefore making an instance of this class will automatically connect to the single mode server by calling this function.   
�̱� ��� ������ �������ְ� field `redStones`�� ������ ��ġ�� �־��ش�. �� method�� constructor���� �θ��� ������ ���� �ҷ��� �ʿ䰡 ����.
<br><br>
__Parameter__
    - `ip`   
    String type that contains the ip information.   
    ip ������ ������ �ִ� ���ڿ�.   
        - Ex) "127.0.0.1"
    - `port`   
    Integer type that contains the port number information.   
    port ������ ������ �ִ� ����.
        - Ex) 8080
    - `color`   
    String type that contains the color of the stone that the client will be using.   
    ����ڰ� ����� ���� �� ������ ������ �ִ� ���ڿ�.
        - `White` `Black`   
       
    __Returns__
    - A String that contatins the positions of the red stones. The positions will follow the `strict notation` explained above.   
    ������ ��ǥ ������ ��� ���ڿ��� �����Ѵ�. �� ��ǥ���� ������ ������ `strict notation`�� ������.   
       
    __Throws__
    - `ConnSixException` Throws an exception that happens when the network connection fail.
    Connection failure can happen because of ip, port information error, underlying protocol error and IOException related to socket creation.   
    ��Ʈ��ũ ������ �����ϸ� `ConnSixException` ���ܸ� ������. ���� ���д� �߸��� ip, port ����, �������� ���� �׸��� socket ���� ���� IOException ������ �߻��� �� �ִ�.   

3. ***public String drawAndRead(String draw)***   
Sends the position of the user's next move to the single mode server and returns the opponent's next move.
The first move of black must be "K10" and the first move of white must be "", an empty String.
If the user sends an invalid coordinate, an error message will be sent to the single mode server.
All positions will follow the position notation explained above.   
������� ���� ���� �̱� ��� ������ ������ ������ ���� ���� �����Ѵ�.
�浹�� ù ���� ������ `K10`�̾���ϰ� �鵹�� ù ���� ������ �� ���ڿ��̾���Ѵ�.
���� ����ڰ� ��ȿ���� ���� ��ǥ�� �����ٸ� ������ ���� ���� �ƴ� ���� �޼����� ���ϵ� ���̴�.
��� ��ǥ���� ������ ������ ��ǥ ǥ����� ������Ѵ�.
    1. `BADCOORD`
        - The coordinate if out of bounds.   
        ������ ��� ��ǥ�� ���
    2. `NOTEMPTY`
        - The position is already occupied by another stone.   
        ������� ���� ��ġ�� ��ǥ�� ���
    3. `BADINPUT`
        - The first move is not `K10` for black or an empty string for white.   
        �浹�� ù ���� `K10`�� �ƴϰų� �鵹�� ù ���� �� ���ڿ��� �ƴ� ���
        - The moves other than the first move don't hold two positions.   
        ù ���� �ƴѵ� ���� �ΰ� ������ ���� ���
        - Any other inputs that doesn't follow the position notation.   
        �� �� ��ǥ ǥ����� ������ �ʴ� �Է��� ���   
           
    __Parameter__   
    - `draw`   
    The position where the user will put their stones.   
    ������� ���� ���� ǥ��� ���ڿ�   
       
    __Returns__
    - When the game continues, the position of the opponent's move, expressed in `strict notation`, will be returned.
    When the game is over, the return value will be `WIN`, `LOSE` or `TIE`.   
    ������ ����� ���, `strict noation`�� ǥ��� ������ ���� ���� �����Ѵ�.
    ������ ���� ���, ���� ����� ���� `WIN`, `LOSE` Ȥ�� `TIE`�� ���ϵȴ�.   
       
    __Throws__
    - `ConnSixException`   
    Throws an exception when communication with the single mode server failed.   
    �̱� ��� �������� ����� �������� ��� ���ܸ� ������.

4. ***public String getStoneAt(String position)***   
Returns the current state of the position.   
�Է����� ���� ��ǥ�� ���� ���¸� �����Ѵ�.
<br><br>
__Parameter__
    - `position`   
    The position of the state that the user is curious about.   
    ����ڰ� ����� ������ ��ġ ��ǥ   
       
    __Returns__
    - Returns a String that can be `EMPTY`, `WHITE`, `BLACK` or `RED` according to the state of the position.
    When the position does not follow the position notation, the function will return the null value.   
    ��ǥ�� ���¿� ���� `EMPTY`, `WHITE`, `BLACK` Ȥ�� `RED` ���� ���ڿ��� �����Ѵ�.
    �Է� ��ǥ�� ��ǥ ǥ����� ������ �ʴ� ��� null ���� �����Ѵ�.   

## Dummy AI
There is an example that randomly generates 2 coordinates and sends to the single mkode server.
Due to the fact that it just generates coordinates, it may send invalid coordinates and end the game.   
'dummyAi'��� ���� �ȿ��� �� API�� ����ϴ� ���� ���α׷��� �ִ�.
�� ���α׷��� �������� �ΰ��� ��ǥ�� �����س� API���� method���� �̿��� �̱� ��� ������ ������.
�̶� �������� �����ϴ� �� ��ǥ�� ��ȿ�� ��ǥ������ Ȯ������ �ʱ� ������ ��ȿ���� ���� ��ǥ�� ���� �̱� ��� �������� ������ ������ �� �ִ�.
