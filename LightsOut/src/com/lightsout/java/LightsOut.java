package com.lightsout.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class LightsOut {

	private static int clicks = 0;
	private static int solutionMode = 0;

	public static void GameWindow() throws IOException {

		JFrame frame2 = new JFrame();
		String name = JOptionPane.showInputDialog(frame2, "Enter your name:");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1180, 1000);
		frame.setLayout(null);
		frame.setResizable(false);

		JPanel lightsPanel = new JPanel();
		lightsPanel.setSize(400, 400);
		lightsPanel.setLayout(new GridLayout(5,5));
		lightsPanel.setBounds(330,300,400,400);
		frame.add(lightsPanel);

		JButton light[][] = new JButton[5][5];
		boolean isLit[][] = new boolean[5][5];
		int solutionTable [][] = new int[5][5];
		int solutionTableInitial [][] = new int[5][5];
		int moves[] = new int[10];

		for(int i = 0; i < 10; i++) {
			moves[i] = Integer.MAX_VALUE;
		}

		boolean lightInitial[][] = new boolean[5][5];

		JLabel title = new JLabel("Lights Out");
		title.setBounds(430,20,400,100);
		title.setFont(new Font("Serif", Font.PLAIN, 48));
		frame.add(title);

		JLabel instructions = new JLabel("Get rid of all the yellow squares!!!");
		instructions.setBounds(370,200,400,100);
		instructions.setFont(new Font("Serif", Font.PLAIN, 24));
		frame.add(instructions);

		JLabel numberOfClicks = new JLabel("Number of Moves: " + clicks);
		numberOfClicks.setBounds(750, 360, 300, 100);
		numberOfClicks.setFont(new Font("Serif", Font.PLAIN, 24));
		frame.add(numberOfClicks);

		JLabel winMessage = new JLabel("Game in progress...");
		winMessage.setBounds(330, 700, 800, 100);
		winMessage.setFont(new Font("Serif", Font.PLAIN, 32));
		frame.add(winMessage);

		JLabel currentPlayer = new JLabel("Current Player: "+name);
		currentPlayer.setBounds(750, 280, 800, 100);
		currentPlayer.setFont(new Font("Serif", Font.PLAIN, 32));
		frame.add(currentPlayer);

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				isLit[i][j] = false;
				light[i][j] = new JButton();
				solutionTable[i][j] = 0;
			}
		}
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				lightsPanel.add(light[i][j]);
			}
		}
		randomize(light, isLit, solutionTable);

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				lightInitial[i][j] = isLit[i][j];
				solutionTableInitial[i][j] = solutionTable[i][j];
			}
		}

		frame.add(lightsPanel);

		JButton solution = new JButton("Solution");
		solution.setBounds(30, 390, 117, 50);
		frame.add(solution);

		JButton replay = new JButton("New Game");
		replay.setBounds(30, 300, 117, 50);
		frame.add(replay);

		JButton restart = new JButton("Restart");
		restart.setBounds(177, 300, 117, 50);
		frame.add(restart);

		JButton highScores = new JButton("Check Highscores");
		highScores.setFont(new Font("Serif", Font.PLAIN, 32));
		highScores.setBounds(750,800,300,100);
		highScores.setBackground(Color.white);
		frame.add(highScores);

		JButton loadBoard = new JButton("Load Game");
		loadBoard.setBounds(177,390,117,50);
		frame.add(loadBoard);

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				light[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						if(solutionMode == 0) {
							JButton source = (JButton) e.getSource();
							mouseClick(isLit, source, light, solutionTable);
							clicks++;
							numberOfClicks.setText("Number of Moves: " + clicks);
							if (clicks == 5) {
								int n = JOptionPane.showConfirmDialog (frame, "Would you like to give up?","Reminder", JOptionPane.YES_NO_OPTION);

								if (n == JOptionPane.YES_OPTION) {
									for(int i = 0; i < 5; i++) {
										for(int j = 0; j < 5; j++) {
											light[i][j].setBackground(Color.RED);
										}
									}
									winMessage.setBounds(200, 700, 800, 100);
									winMessage.setText("You have given up. Please start a new game.");
								}
								else {
									frame.setVisible(true);
								}
							}
						}
						else if(solutionMode == 1) {
							JButton source = (JButton) e.getSource();
							mouseClickSolutionMode(solutionTable, light,source, isLit);
							clicks++;
							numberOfClicks.setText("Number of Moves: " + clicks);
						}
						int count = 0;

						if(checkWin(isLit)==true) {
							count++;
							for(int i = 0; i < 5; i++) {
								for(int j = 0; j < 5; j++) {
									light[i][j].setBackground(Color.GREEN);
								}
							}
							winMessage.setText("You won in "+clicks+" moves");
							if(count>10) {
								sortHighscores(moves);
								if(moves[9] > clicks) {
									moves[9] = clicks;
								}
							}
							for(int i = 0; i < 10; i++) {
								if(moves[i] == Integer.MAX_VALUE) {
									moves[i] = clicks;
									break;
								}
							}
						}
					}
				});

			}
		}

		solution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub+
				if(solutionMode == 1 || solutionMode == 2) {
					solutionMode=0;
					mouseClickSolutionButton(light, solutionMode, solutionTable);
				}
				else if(solutionMode == 0) {
					solutionMode = 1;
					mouseClickSolutionButton(light, solutionMode, solutionTable);
				}

			}

		});

		replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				solutionMode = 0;
				mouseClickReplayButton(solutionTable, light, isLit, lightInitial, solutionTableInitial);
				clicks = 0;
				numberOfClicks.setText("Number of Moves: " + clicks);
				for(int i = 0; i < 10; i++) {
					moves[i] = Integer.MAX_VALUE;
				}
				winMessage.setBounds(300, 700, 800, 100);
				winMessage.setText("Game in progress...");
			}
		});

		restart.addActionListener(new ActionListener() {
			@Override  
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				solutionMode = 0;
				mouseClickRestartButton(light, isLit, lightInitial);	
				for(int i = 0; i < 5; i++) {
					for(int j = 0; j < 5; j++) {
						solutionTable[i][j] = solutionTableInitial[i][j];
					}
				}
				clicks = 0;
				numberOfClicks.setText("Number of Moves: " + clicks);
				winMessage.setBounds(300, 700, 800, 100);
				winMessage.setText("Game in progress...");
			}
		});

		highScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sortHighscores(moves);
				JFrame leaderboard = new JFrame();
				JLabel highscore = new JLabel();
				highscore.setText("LEADERBOARDS (TOP 10)");
				highscore.setBounds(0,0,300,100);
				highscore.setFont(new Font("Serif", Font.PLAIN, 24));
				leaderboard.add(highscore);
				
				JLabel one = new JLabel();
				one.setText("1. "+moves[0]);
				one.setBounds(0,100,300,50);
				one.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[0] != Integer.MAX_VALUE) {
					leaderboard.add(one);
				}
				
				JLabel two = new JLabel();
				two.setText("2. "+ moves[1]);
				two.setBounds(0,150,300,50);
				two.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[1] != Integer.MAX_VALUE) {
					leaderboard.add(two);
				}
				JLabel three = new JLabel();
				three.setText("3. "+ moves[2]);
				three.setBounds(0,200,300,50);
				three.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[2] != Integer.MAX_VALUE) {
					leaderboard.add(three);
				}
				
				JLabel four = new JLabel();
				four.setText("4. "+ moves[3]);
				four.setBounds(0,250,300,50);
				four.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[3] != Integer.MAX_VALUE) {
					leaderboard.add(four);
				}
				
				JLabel five = new JLabel();
				five.setText("5. "+ moves[4]);
				five.setBounds(0,300,300,50);
				five.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[4] != Integer.MAX_VALUE) {
					leaderboard.add(five);
				}
				
				JLabel six = new JLabel();
				six.setText("6. "+moves[5]);
				six.setBounds(0,350,300,50);
				six.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[5] != Integer.MAX_VALUE) {
					leaderboard.add(six);
				}
				
				JLabel seven = new JLabel();
				seven.setText("7. "+ moves[6]);
				seven.setBounds(0,400,300,50);
				seven.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[6] != Integer.MAX_VALUE) {
					leaderboard.add(seven);
				}
				
				JLabel eight = new JLabel();
				eight.setText("8. "+moves[7]);
				eight.setBounds(0,450,300,50);
				eight.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[7] != Integer.MAX_VALUE) {
					leaderboard.add(eight);
				}
				
				JLabel nine = new JLabel();
				nine.setText("9. "+moves[8]);
				nine.setBounds(0,500,300,50);
				nine.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[8] != Integer.MAX_VALUE) {
					leaderboard.add(nine);
				}
				
				JLabel ten = new JLabel();
				ten.setText("10. "+moves[9]);
				ten.setBounds(0,550,300,50);
				ten.setFont(new Font("Serif", Font.PLAIN, 24));
				if(moves[9] != Integer.MAX_VALUE) {
					leaderboard.add(ten);
				}
				
				
				leaderboard.setSize(300, 1000);
				leaderboard.setLayout(null);
				leaderboard.setResizable(false);
				leaderboard.setVisible(true);
			}
		});

		loadBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solutionMode = 0;
				try {
					equal(isLit, fileread());
					equal(lightInitial, fileread());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					translate(fileread(), light);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(int i = 0; i < 5; i++) {
					for(int j = 0; j < 5; j++) {
						solutionTable[i][j] = 1;
						solutionTableInitial[i][j] = 1;
					}
				}
			}
			
		});

		frame.setVisible(true);

	}

	public static String nameSwitch(String name, String name1) {
		name = name1;
		return name;
	}
	public static void changePlayer(Object name) {
		JFrame frame2 = new JFrame();
		name = JOptionPane.showInputDialog(frame2, "Enter your name:");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	public static void equal(boolean isLit[][], boolean table[][]) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				isLit[i][j] = table[i][j];
			}
		}
	}

	public static void sortHighscores(int moves[]) {
		for(int i = 0; i < moves.length-1; i++) {
			for(int j = 1; j < moves.length-i; j++) {
				if(moves[j-1] > moves[j]) {
					int temp = 0;
					temp = moves[j];
					moves[j] = moves[j-1];
					moves[j-1] = temp;
				}
			}
		}
	}

	public static void mouseClick(boolean isLit[][], JButton source, JButton light[][], int[][]solutionTable) {
		int row = 0,col = 0;
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if(source == light[i][j]) {
					row = i; 
					col = j;
				}
			}
		}
		solution(solutionTable, row, col);
		toggle(row, col, isLit, light);
	}

	public static void mouseClickSolutionButton(JButton light[][], int solutionMode, int solutionTable[][]) {
		if(solutionMode == 0) {
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					light[i][j].setText(null);
				}
			}
		}
		if(solutionMode == 1) {
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					solutionOutput(solutionTable, light);
				}
			}
		}
	}

	public static void mouseClickSolutionMode(int solutionTable[][], JButton light[][], JButton source, boolean isLit[][]) {
		int row = 0,col = 0;
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if(source == light[i][j]) {
					row = i; 
					col = j;
				}
			}
		}

		if (solutionTable[row][col] == 1) {
			solutionTable[row][col] = 0;
		}
		else if (solutionTable[row][col] == 0) {
			solutionTable[row][col] = 1;
		}
		solutionOutput(solutionTable, light);
		toggle(row, col, isLit, light);
	}

	public static void mouseClickReplayButton(int[][]solutionTable, JButton light[][], boolean isLit[][], boolean lightInitial[][], int solutionTableInitial[][]) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				solutionTable[i][j] = 0;
				isLit[i][j] = false;
				light[i][j].setText("");
			}
		}

		randomize(light, isLit, solutionTable);
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				lightInitial[i][j] = isLit[i][j];
				solutionTableInitial[i][j] = solutionTable[i][j];
			}
		}
	}

	public static void mouseClickRestartButton(JButton light[][], boolean isLit[][], boolean lightInitial[][]) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				isLit[i][j] = lightInitial[i][j];
				light[i][j].setText("");
				if(isLit[i][j] == false) {
					light[i][j].setBackground(null);
				}
				else if(isLit[i][j] == true) {
					light[i][j].setBackground(Color.ORANGE);
				}
			}
		}

	}


	public static void randomize(JButton light[][], boolean isLit[][], int[][] solutionTable) {
		int row, col;
		int count = 0, count1 = 0;

		while(true) {
			count = 0;

			row = (int) (Math.round(Math.random()*4));
			col = (int) (Math.round(Math.random()*4));
			solution(solutionTable, row, col);
			toggle(row,col,isLit,light);
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					if(isLit[i][j] == true) {
						count++;
					}
				}
			}
			if(count == 10) {
				count1++;
				if(count1 == 5) {
					break;
				}
			}
		}
	}

	public static void solution(int[][] solutionTable, int row, int col) {
		solutionTable[row][col]++;
		if (solutionTable[row][col] == 2) {
			solutionTable[row][col] = 0;
		}
	}
	public static void solutionOutput(int [][]solutionTable, JButton light [][]) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if (solutionTable[i][j] == 1){
					light[i][j].setText("o");
				}else if (solutionTable[i][j] == 0) {
					light[i][j].setText(" ");
				}
			}
		}
	}

	public static void toggle(int row, int col, boolean isLit[][], JButton light[][]) {
		if (isLit[row][col] == false) {
			light[row][col].setBackground(Color.ORANGE);
			isLit[row][col] = true;
			if (row > 0) {
				if (isLit[row-1][col] == false) {
					light[row-1][col].setBackground(Color.ORANGE);
					isLit[row-1][col] = true;
				} else if (isLit[row - 1][col] == true) {
					light[row-1][col].setBackground(null);
					isLit[row-1][col] = false;
				}
			}
			if (col < 4) {
				if (isLit[row][col+1] == false) {
					light[row][col+1].setBackground(Color.ORANGE);
					isLit[row][col+1] = true;
				} else if (isLit[row][col + 1] == true) {
					light[row][col+1].setBackground(null);
					isLit[row][col+1] = false;
				}
			}
			if (row < 4) {
				if (isLit[row+1][col] == false) {
					light[row+1][col].setBackground(Color.ORANGE);
					isLit[row+1][col] = true;
				} else if (isLit[row+1][col] == true) {
					light[row+1][col].setBackground(null);
					isLit[row+1][col] = false;
				}
			}
			if (col > 0) {
				if (isLit[row][col-1] == false) {
					light[row][col-1].setBackground(Color.ORANGE);
					isLit[row][col-1] = true;
				} else if (isLit[row][col - 1] == true) {
					light[row][col-1].setBackground(null);
					isLit[row][col-1] = false;
				}
			}
		}

		else if (isLit[row][col] == true) {
			light[row][col].setBackground(null);
			isLit[row][col] = false;
			if (row > 0) {
				if (isLit[row-1][col] == false) {
					light[row-1][col].setBackground(Color.ORANGE);
					isLit[row-1][col] = true;
				} else if (isLit[row-1][col] == true) {
					light[row-1][col].setBackground(null);
					isLit[row-1][col] = false;
				}
			}
			if (col < 4) {
				if (isLit[row][col+1] == false) {
					light[row][col+1].setBackground(Color.ORANGE);
					isLit[row][col+1] = true;
				} else if (isLit[row][col+1] == true) {
					light[row][col+1].setBackground(null);
					isLit[row][col+1] = false;
				}
			}
			if (row < 4) {
				if (isLit[row+1][col] == false) {
					light[row+1][col].setBackground(Color.ORANGE);
					isLit[row+1][col] = true;
				} else if (isLit[row+1][col] == true) {
					light[row+1][col].setBackground(null);
					isLit[row+1][col] = false;
				}
			}
			if (col > 0) {
				if (isLit[row][col-1] == false) {
					light[row][col-1].setBackground(Color.ORANGE);
					isLit[row][col-1] = true;
				} else if (isLit[row][col-1] == true) {
					light[row][col-1].setBackground(null);
					isLit[row][col-1] = false;
				}
			}
		}
	}

	public static boolean checkWin(boolean isLit[][]) {
		boolean checkWin = false;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (isLit[i][j] == true) {
					checkWin = false;
					break;
				} else if (isLit[i][j] == false) {
					checkWin = true;
				}
			}
			if (checkWin == false) {
				break;
			}
		}

		if (checkWin == true) {
			return true;
		}
		else {
			return false;
		}

	}

	public static void translate(boolean isLit[][], JButton light[][]) {
		for(int i=0;i<isLit.length;i++) {
			for(int j=0;j<isLit.length;j++) {
				if(isLit[i][j]==true) {
					light[i][j].setBackground(Color.ORANGE);
				}
				else {
					light[i][j].setBackground(null);
				}
			}
		}
	}

	public static boolean [][] fileread() throws IOException{
		BufferedReader br =  new BufferedReader(new FileReader("C:\\JavaProjectData\\board.txt"));
		boolean [][] table = new boolean[5][5];
		String str;
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				str=br.readLine();
				table[i][j]=Boolean.parseBoolean(str);
			}
		}
		br.close();
		return table;
	}



	public static void main(String[] args) throws IOException{
		GameWindow();
	}


}