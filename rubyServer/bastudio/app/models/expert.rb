class Expert < ApplicationRecord
  has_many :exp_procedures
  has_many :procedures, through: :exp_procedures

  def as_json(options={})
   super(options.merge methods: [:procedures_json])
 end

 def procedures_json
    procedures.as_json(expertId: self.id)
  end

  def exp_procedures
    exp_procedures.as_json
  end

end
