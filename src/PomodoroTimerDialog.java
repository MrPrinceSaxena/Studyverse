import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

public class PomodoroTimerDialog extends JDialog {
    private static final int POMODORO_MIN = 25;
    private static final int BREAK_MIN = 5;

    private Timer timer;
    private int secondsLeft;
    private boolean isBreak = false;

    private JLabel timerLabel;
    private JButton startBtn, stopBtn, resetBtn, completeTaskBtn;
    private JComboBox<String> sessionTypeBox;

    public PomodoroTimerDialog(Frame owner, Runnable onTaskComplete) {
        super(owner, "Pomodoro Timer", true);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        timerLabel = new JLabel(formatTime(POMODORO_MIN * 60), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel controlPanel = new JPanel();
        startBtn = new JButton("Start");
        stopBtn = new JButton("Pause");
        resetBtn = new JButton("Reset");
        sessionTypeBox = new JComboBox<>(new String[]{"Pomodoro (25m)", "Break (5m)"});
        completeTaskBtn = new JButton("Complete Task");
        completeTaskBtn.setEnabled(onTaskComplete != null);

        controlPanel.add(sessionTypeBox);
        controlPanel.add(startBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(completeTaskBtn);

        add(timerLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Button listeners
        startBtn.addActionListener(e -> startTimer());
        stopBtn.addActionListener(e -> pauseTimer());
        resetBtn.addActionListener(e -> resetTimer());
        completeTaskBtn.addActionListener(e -> {
            if (onTaskComplete != null) onTaskComplete.run();
            setVisible(false);
        });

        sessionTypeBox.addActionListener(e -> resetTimer());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);

        resetTimer();
    }

    private void startTimer() {
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        sessionTypeBox.setEnabled(false);

        timer = new Timer(1000, e -> {
            secondsLeft--;
            timerLabel.setText(formatTime(secondsLeft));
            if (secondsLeft <= 0) {
                timer.stop();
                playSound();
                JOptionPane.showMessageDialog(this, isBreak ? "Break finished!" : "Pomodoro session finished! Take a break.");
                // Auto-switch between session and break
                if (!isBreak) {
                    sessionTypeBox.setSelectedIndex(1);
                } else {
                    sessionTypeBox.setSelectedIndex(0);
                }
                resetTimer();
            }
        });
        timer.start();
    }

    private void pauseTimer() {
        if (timer != null) timer.stop();
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    private void resetTimer() {
        if (timer != null) timer.stop();
        isBreak = sessionTypeBox.getSelectedIndex() == 1;
        secondsLeft = (isBreak ? BREAK_MIN : POMODORO_MIN) * 60;
        timerLabel.setText(formatTime(secondsLeft));
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        sessionTypeBox.setEnabled(true);
    }

    private String formatTime(int totalSeconds) {
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void playSound() {
        // Simple beep; for a custom sound, load a .wav file
        Toolkit.getDefaultToolkit().beep();
    }
}