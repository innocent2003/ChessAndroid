package com.murach.myapplication



import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player

import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDelegate {
    private val socketHost = "127.0.0.1"
    private val socketPort: Int = 50000
    private val socketGuestPort: Int = 50001 // used for socket server on emulator
    private lateinit var chessView: ChessView
    private lateinit var resetButton: Button
    private lateinit var listenButton: Button
    private lateinit var connectButton: Button
    private var printWriter: PrintWriter? = null
    private var serverSocket: ServerSocket? = null
    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ChessGame.initialize(applicationContext)

        chessView = findViewById(R.id.chess_view)
        resetButton = findViewById(R.id.reset_button)
        val promoteBtn = findViewById<Button>(R.id.btnPromote)
//        listenButton = findViewById(R.id.listen_button)
//        connectButton = findViewById(R.id.connect_button)
        chessView.chessDelegate = this
        promoteBtn.setOnClickListener{
            showPopupWindow()
        }

        resetButton.setOnClickListener {
            ChessGame.reset()
            chessView.invalidate()
//            serverSocket?.close()
//            listenButton.isEnabled = true
        }


//        listenButton.setOnClickListener {
//            listenButton.isEnabled = false
//            val port = if (isEmulator) socketGuestPort else socketPort
//            Toast.makeText(this, "listening on $port", Toast.LENGTH_SHORT).show()
//            Executors.newSingleThreadExecutor().execute {
//                ServerSocket(port).let { srvSkt ->
//                    serverSocket = srvSkt
//                    try {
//                        val socket = srvSkt.accept()
//                        receiveMove(socket)
//                    } catch (e: SocketException) {
//                        // ignored, socket closed
//                    }
//                }
//            }
//        }
//
//        connectButton.setOnClickListener {
//            Log.d(TAG, "socket client connecting ...")
//            Executors.newSingleThreadExecutor().execute {
//                try {
//                    val socket = Socket(socketHost, socketPort)
//                    receiveMove(socket)
//                } catch (e: ConnectException) {
//                    runOnUiThread {
//                        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

    private fun receiveMove(socket: Socket) {
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while (scanner.hasNextLine()) {
            val move = scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread {
                ChessGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))
                chessView.invalidate()
                checkGameStatus()
            }
        }
    }

    override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        ChessGame.movePiece(from, to)
        chessView.invalidate()
        checkGameStatus()

//        printWriter?.let {
//            val moveStr = "${from.col},${from.row},${to.col},${to.row}"
//            Executors.newSingleThreadExecutor().execute {
//                it.println(moveStr)
//            }
//        }
    }

 fun onPawnPromotion(square: Square) {
//        val promotionPopup = layoutInflater.inflate(R.layout.promotion_popup, null)
//        val dialog = AlertDialog.Builder(this)
//            .setView(promotionPopup)
//            .setCancelable(false)
//            .create()
//
//        promotionPopup.findViewById<Button>(R.id.promo_queen).setOnClickListener {
//            promotePawn(square, Chessman.QUEEN)
//            dialog.dismiss()
//        }
//        promotionPopup.findViewById<Button>(R.id.promo_rook).setOnClickListener {
//            promotePawn(square, Chessman.ROOK)
//            dialog.dismiss()
//        }
//        promotionPopup.findViewById<Button>(R.id.promo_bishop).setOnClickListener {
//            promotePawn(square, Chessman.BISHOP)
//            dialog.dismiss()
//        }
//        promotionPopup.findViewById<Button>(R.id.promo_knight).setOnClickListener {
//            promotePawn(square, Chessman.KNIGHT)
//            dialog.dismiss()
//        }

//        dialog.show()
    }
    private fun promotePawn(square: Square, chessman: Chessman) {
//        val player = ChessGame.pieceAt(square)?.player ?: return
//        val drawableRes = when (chessman) {
//            Chessman.QUEEN -> if (player == Player.WHITE) R.drawable.queen_white else R.drawable.queen_black
//            Chessman.ROOK -> if (player == Player.WHITE) R.drawable.rook_white else R.drawable.rook_black
//            Chessman.BISHOP -> if (player == Player.WHITE) R.drawable.bishop_white else R.drawable.bishop_black
//            Chessman.KNIGHT -> if (player == Player.WHITE) R.drawable.knight_white else R.drawable.knight_black
//            else -> return
//        }
//
//        ChessGame.piecesBox[square] = ChessPiece(player, chessman, drawableRes)
//        ChessGame.turn = if (ChessGame.turn == Player.WHITE) Player.BLACK else Player.WHITE
//        chessView.invalidate()
//        checkGameStatus()


    }

    private fun checkGameStatus() {
        when {
            ChessGame.isCheckmate(Player.WHITE) -> showToast("Checkmate! Black wins.")
            ChessGame.isCheckmate(Player.BLACK) -> showToast("Checkmate! White wins.")
            ChessGame.isStalemate(Player.WHITE) || ChessGame.isStalemate(Player.BLACK) -> showToast("Stalemate!")
            ChessGame.isCheck(Player.WHITE) -> showToast("White is in check.")
            ChessGame.isCheck(Player.BLACK) -> showToast("Black is in check.")
//            ChessGame.promotePawn()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private  fun showPopupWindow() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.blackpromote, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // Show the popup window
        popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0)

        // Close the popup window on button click
        val buttonClose: Button = popupView.findViewById(R.id.button_close)
        val queenBtn : Button = popupView.findViewById(R.id.promo_queen)
        queenBtn.setOnClickListener{
            popupWindow.dismiss()
        }

    }
}