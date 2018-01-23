// Copyright 2017 Dolphin Emulator Project
// Licensed under GPLv2+
// Refer to the license.txt file included.

#include "DolphinQt2/Debugger/NewBreakpointDialog.h"

#include <QCheckBox>
#include <QDialogButtonBox>
#include <QGridLayout>
#include <QGroupBox>
#include <QHBoxLayout>
#include <QLabel>
#include <QLineEdit>
#include <QMessageBox>
#include <QRadioButton>
#include <QVBoxLayout>

#include "DolphinQt2/Debugger/BreakpointWidget.h"

NewBreakpointDialog::NewBreakpointDialog(BreakpointWidget* parent)
    : QDialog(parent), m_parent(parent)
{
  setWindowTitle(tr("New Breakpoint"));
  CreateWidgets();
  ConnectWidgets();

  OnBPTypeChanged();
  OnAddressTypeChanged();
}

void NewBreakpointDialog::CreateWidgets()
{
  m_buttons = new QDialogButtonBox(QDialogButtonBox::Ok | QDialogButtonBox::Cancel);

  // Instruction BP
  m_instruction_bp = new QRadioButton(tr("Instruction Breakpoint"));
  m_instruction_bp->setChecked(true);
  m_instruction_box = new QGroupBox;
  m_instruction_address = new QLineEdit;

  auto* instruction_layout = new QHBoxLayout;
  m_instruction_box->setLayout(instruction_layout);
  instruction_layout->addWidget(new QLabel(tr("Address:")));
  instruction_layout->addWidget(m_instruction_address);

  // Memory BP
  m_memory_bp = new QRadioButton(tr("Memory Breakpoint"));
  m_memory_box = new QGroupBox;
  m_memory_use_address = new QRadioButton(tr("Address"));
  m_memory_use_address->setChecked(true);
  m_memory_use_range = new QRadioButton(tr("Range"));
  m_memory_address_from = new QLineEdit;
  m_memory_address_to = new QLineEdit;
  m_memory_address_from_label = new QLabel;  // Set by OnAddressTypeChanged
  m_memory_address_to_label = new QLabel(tr("To:"));
  m_memory_on_read = new QRadioButton(tr("Read"));
  m_memory_on_write = new QRadioButton(tr("Write"));
  m_memory_on_read_and_write = new QRadioButton(tr("Read or Write"));
  m_memory_on_write->setChecked(true);
  m_memory_do_log = new QRadioButton(tr("Log"));
  m_memory_do_break = new QRadioButton(tr("Break"));
  m_memory_do_log_and_break = new QRadioButton(tr("Log and Break"));
  m_memory_do_log_and_break->setChecked(true);

  auto* memory_layout = new QGridLayout;
  m_memory_box->setLayout(memory_layout);
  memory_layout->addWidget(m_memory_use_address, 0, 0);
  memory_layout->addWidget(m_memory_use_range, 0, 3);
  memory_layout->addWidget(m_memory_address_from_label, 1, 0);
  memory_layout->addWidget(m_memory_address_from, 1, 1);
  memory_layout->addWidget(m_memory_address_to_label, 1, 2);
  memory_layout->addWidget(m_memory_address_to, 1, 3);
  memory_layout->addWidget(new QLabel(tr("On...")), 2, 0);
  memory_layout->addWidget(m_memory_on_read, 2, 1);
  memory_layout->addWidget(m_memory_on_write, 2, 2);
  memory_layout->addWidget(m_memory_on_read_and_write, 2, 3);
  memory_layout->addWidget(new QLabel(tr("Do...")), 3, 0);
  memory_layout->addWidget(m_memory_do_log, 3, 1);
  memory_layout->addWidget(m_memory_do_break, 3, 2);
  memory_layout->addWidget(m_memory_do_log_and_break, 3, 3);

  auto* layout = new QVBoxLayout;

  layout->addWidget(m_instruction_bp);
  layout->addWidget(m_instruction_box);
  layout->addWidget(m_memory_bp);
  layout->addWidget(m_memory_box);
  layout->addWidget(m_buttons);

  setLayout(layout);
}

void NewBreakpointDialog::ConnectWidgets()
{
  connect(m_buttons, &QDialogButtonBox::accepted, this, &NewBreakpointDialog::accept);
  connect(m_buttons, &QDialogButtonBox::rejected, this, &NewBreakpointDialog::reject);

  connect(m_instruction_bp, &QRadioButton::toggled, this, &NewBreakpointDialog::OnBPTypeChanged);
  connect(m_memory_bp, &QRadioButton::toggled, this, &NewBreakpointDialog::OnBPTypeChanged);

  connect(m_memory_use_address, &QRadioButton::toggled, this,
          &NewBreakpointDialog::OnAddressTypeChanged);
  connect(m_memory_use_range, &QRadioButton::toggled, this,
          &NewBreakpointDialog::OnAddressTypeChanged);
}

void NewBreakpointDialog::OnBPTypeChanged()
{
  m_instruction_box->setEnabled(m_instruction_bp->isChecked());
  m_memory_box->setEnabled(m_memory_bp->isChecked());
}

void NewBreakpointDialog::OnAddressTypeChanged()
{
  bool ranged = m_memory_use_range->isChecked();

  m_memory_address_to->setHidden(!ranged);
  m_memory_address_to_label->setHidden(!ranged);

  m_memory_address_from_label->setText(ranged ? tr("From:") : tr("Address:"));
}

void NewBreakpointDialog::accept()
{
  auto invalid_input = [this](QString field) {
    QMessageBox::critical(this, tr("Error"), tr("Bad input provided for %1 field").arg(field));
  };

  bool instruction = m_instruction_bp->isChecked();
  bool ranged = m_memory_use_range->isChecked();

  // Triggers
  bool on_read = m_memory_on_read->isChecked() || m_memory_on_read_and_write->isChecked();
  bool on_write = m_memory_on_write->isChecked() || m_memory_on_read_and_write->isChecked();

  // Actions
  bool do_log = m_memory_do_log->isChecked() || m_memory_do_log_and_break->isChecked();
  bool do_break = m_memory_do_break->isChecked() || m_memory_do_log_and_break->isChecked();

  bool good;

  if (instruction)
  {
    u32 address = m_instruction_address->text().toUInt(&good, 16);

    if (!good)
    {
      invalid_input(tr("address"));
      return;
    }

    m_parent->AddBP(address);
  }
  else
  {
    u32 from = m_memory_address_from->text().toUInt(&good, 16);

    if (!good)
    {
      invalid_input(ranged ? tr("from") : tr("address"));
      return;
    }

    if (ranged)
    {
      u32 to = m_memory_address_to->text().toUInt(&good, 16);
      if (!good)
      {
        invalid_input(tr("to"));
        return;
      }

      m_parent->AddRangedMBP(from, to, on_read, on_write, do_log, do_break);
    }
    else
    {
      m_parent->AddAddressMBP(from, on_read, on_write, do_log, do_break);
    }
  }

  QDialog::accept();
}
