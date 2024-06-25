package com.murach.myapplication.WithComputer

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
<<<<<<< HEAD
=======
import androidx.appcompat.app.AlertDialog
import com.murach.myapplication.AllActivity.ChessSettings
>>>>>>> tmquangFeature3
import com.murach.myapplication.AllActivity.MainMenu
import com.murach.myapplication.R
import com.murach.myapplication.WithOffline.ChessDelegate
import com.murach.myapplication.WithOffline.ChessGame
import com.murach.myapplication.WithOffline.ChessPiece
import com.murach.myapplication.WithOffline.ChessView
import com.murach.myapplication.WithOffline.Square
import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player
import java.io.PrintWriter
import java.net.ServerSocket

class BotActivity  : AppCompatActivity(), ChessDelegate {
    private val socketHost = "127.0.0.1"
    private val socketPort: Int = 50000
    private val socketGuestPort: Int = 50001 // used for socket server on emulator
    private lateinit var chessView: ChessView
    private lateinit var resetButton: ImageButton
    private lateinit var listenButton: Button
    private lateinit var connectButton: Button
    private lateinit var settingsButton: ImageButton
    private lateinit var backButton : ImageButton
    private var printWriter: PrintWriter? = null
    private var serverSocket: ServerSocket? = null
    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot)
        ChessGame.initialize(applicationContext)

        chessView = findViewById(R.id.chess_view)
        resetButton = findViewById<ImageButton>(R.id.IconReset)
        settingsButton = findViewById<ImageButton>(R.id.IconSettings)
        backButton = findViewById<ImageButton>(R.id.IconBack)
        settingsButton.setOnClickListener {


        }
        backButton.setOnClickListener{
            ChessGame.reset()
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

            startActivity(Intent(this, ChessSettings::class.java))
        }
        backButton.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.titlle_game)
            builder.setMessage(R.string.exit_game)
            builder.setPositiveButton(R.string.yes_game) { dialog, which ->
                ChessGame.reset()
                startActivity(Intent(this@BotActivity, MainMenu::class.java))
            }
            builder.setNegativeButton(R.string.no_game) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }

//        listenButton = findViewById(R.id.listen_button)
//        connectButton = findViewById(R.id.connect_button)
        chessView.chessDelegate = this


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
    override fun onBackPressed() {
        super.onBackPressed()
        ChessGame.reset()
    }

//    private fun receiveMove(socket: Socket) {
//        val scanner = Scanner(socket.getInputStream())
//        printWriter = PrintWriter(socket.getOutputStream(), true)
//        while (scanner.hasNextLine()) {
//            val move = scanner.nextLine().split(",").map { it.toInt() }
//            runOnUiThread {
//                ChessGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))
//                chessView.invalidate()
//                checkGameStatus()
//            }
//        }
//    }

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
        ChessGame.movePiece(from, to)
        chessView.invalidate()

        val movingPiece = ChessGame.pieceAt(to)
        if (movingPiece != null && movingPiece.chessman == Chessman.PAWN && (to.row == 0 || to.row == 7)) {
            Toast.makeText(this, "Promotion success", Toast.LENGTH_LONG);
        }

        checkGameStatus()
    }


    private fun checkGameStatus() {
        when {
            ChessGame.isCheckmate(Player.WHITE) -> showToast("Checkmate! Black wins.")
            ChessGame.isCheckmate(Player.BLACK) -> showToast("Checkmate! White wins.")
            ChessGame.isStalemate(Player.WHITE) || ChessGame.isStalemate(Player.BLACK) -> showToast(
                "Stalemate!"
            )

            ChessGame.isCheck(Player.WHITE) -> showToast("White is in check.")
            ChessGame.isCheck(Player.BLACK) -> showToast("Black is in check.")
//            ChessGame.whitePawnCheck(ChessPiece(Player.WHITE,Chessman.PAWN,  R.drawable.pawn_white),Square(0,7))-> showToast("Promotion white")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    private fun showPromotionPopup(square: Square) {
//        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val popupView = inflater.inflate(R.layout.popup_promotion, null)
//
//        val width = LinearLayout.LayoutParams.WRAP_CONTENT
//        val height = LinearLayout.LayoutParams.WRAP_CONTENT
//        val focusable = true // lets taps outside the popup also dismiss it
//        val popupWindow = PopupWindow(popupView, width, height, focusable)
//
//        // Show the popup window
//        popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0)
//
//        val queenBtn: Button = popupView.findViewById(R.id.promo_queen)
//        val rookBtn: Button = popupView.findViewById(R.id.promo_rook)
//        val bishopBtn: Button = popupView.findViewById(R.id.promo_bishop)
//        val knightBtn: Button = popupView.findViewById(R.id.promo_knight)
//
//        val onPieceSelected: (Chessman) -> Unit = { selectedPiece ->
//            ChessGame.promotePawn(ChessGame.pieceAt(square)!!, square, ChessGame.piecesBox) {
//                piecesBox[square] = ChessPiece(ChessGame.pieceAt(square)!!.player, selectedPiece, getDrawableForChessman(selectedPiece, ChessGame.pieceAt(square)!!.player))
//                chessView.invalidate()
//                popupWindow.dismiss()
//            }
//        }
//
//        queenBtn.setOnClickListener { onPieceSelected(Chessman.QUEEN) }
//        rookBtn.setOnClickListener { onPieceSelected(Chessman.ROOK) }
//        bishopBtn.setOnClickListener { onPieceSelected(Chessman.BISHOP) }
//        knightBtn.setOnClickListener { onPieceSelected(Chessman.KNIGHT) }
//    }

    private fun getDrawableForChessman(chessman: Chessman, player: Player): Int {
        return when (chessman) {
            Chessman.QUEEN -> if (player == Player.WHITE) R.drawable.queen_white else R.drawable.queen_black
            Chessman.ROOK -> if (player == Player.WHITE) R.drawable.rook_white else R.drawable.rook_black
            Chessman.BISHOP -> if (player == Player.WHITE) R.drawable.bishop_white else R.drawable.bishop_black
            Chessman.KNIGHT -> if (player == Player.WHITE) R.drawable.knight_white else R.drawable.knight_black
            else -> throw IllegalArgumentException("Invalid chessman for promotion")
        }
    }
}